/*
 *
 * Copyright 2015 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.spring.bulkapi.test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.spring.bulkapi.BulkApiException;
import com.github.wnameless.spring.bulkapi.BulkOperation;
import com.github.wnameless.spring.bulkapi.BulkRequest;
import com.github.wnameless.spring.bulkapi.BulkResponse;
import com.github.wnameless.spring.bulkapi.BulkResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class BulkApiTest {

  @Value("${spring.bulk.api.path:/bulk}")
  String bulkPath;

  HttpClient client = HttpClientBuilder.create().build();
  HttpPost post;

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  ApplicationContext appCtx;

  @Before
  public void setUp() {
    post = new HttpPost("http://localhost:8080" + bulkPath);
    post.setHeader("Content-Type", "application/json");
  }

  @Test
  public void testBeans() throws Exception {
    assertThat(BulkOperation.class, allOf(hasValidBeanConstructor(),
        hasValidGettersAndSetters(), hasValidBeanToStringExcluding()));
    EqualsVerifier.forClass(BulkOperation.class)
        .suppress(Warning.NONFINAL_FIELDS).verify();
    assertThat(BulkRequest.class, allOf(hasValidBeanConstructor(),
        hasValidGettersAndSetters(), hasValidBeanToStringExcluding()));
    EqualsVerifier.forClass(BulkRequest.class).suppress(Warning.NONFINAL_FIELDS)
        .verify();
    assertThat(BulkResponse.class, allOf(hasValidBeanConstructor(),
        hasValidGettersAndSetters(), hasValidBeanToStringExcluding()));
    EqualsVerifier.forClass(BulkResponse.class)
        .suppress(Warning.NONFINAL_FIELDS).verify();
    assertThat(BulkResult.class, allOf(hasValidBeanConstructor(),
        hasValidGettersAndSetters(), hasValidBeanToStringExcluding()));
    EqualsVerifier.forClass(BulkResult.class).suppress(Warning.NONFINAL_FIELDS)
        .verify();

    EqualsVerifier.forClass(BulkApiException.class)
        .suppress(Warning.NULL_FIELDS).verify();
  }

  private String operationTimes(int times) {
    times--;

    String ops =
        "{\"method\":\"GET\",\"url\":\"home\",\"headers\":{\"Authorization\":\"Basic "
            + Base64Utils.encodeToString("user:password".getBytes()) + "\"}}";
    String op =
        "{\"method\":\"GET\",\"url\":\"/home\",\"headers\":{\"Authorization\":\"Basic "
            + Base64Utils.encodeToString("user:password".getBytes()) + "\"}}";

    while (times > 0) {
      ops += "," + op;
      times--;
    }

    return ops;
  }

  @Test
  public void testBatch() throws Exception {
    String json = "{\"operations\":[" + operationTimes(1000) + "]}";
    HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);
    String result = EntityUtils.toString(response.getEntity());
    BulkResponse res = new Gson().getAdapter(new TypeToken<BulkResponse>() {})
        .fromJson(result);

    assertTrue(200 == Double.valueOf(res.getResults().get(0).getStatus()));
    assertEquals(1000, res.getResults().size());
  }

  @Test
  public void testOverLimitationError() throws Exception {
    String json = "{\"operations\":[" + operationTimes(1001) + "]}";
    HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);

    assertEquals(413, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testSilentMode() throws Exception {
    String json = "{\"operations\":[" + operationTimes(1)
        + ",{\"method\":\"GET\",\"url\":\"/home\",\"headers\":{\"Authorization\":\"Basic "
        + Base64Utils.encodeToString("user:password".getBytes())
        + "\"},\"silent\":true}]}";
    HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);
    String result = EntityUtils.toString(response.getEntity());
    BulkResponse res = new Gson().getAdapter(new TypeToken<BulkResponse>() {})
        .fromJson(result);

    assertEquals(1, res.getResults().size());
  }

  @Test
  public void testInvalidUrl() throws Exception {
    String json = "{\"operations\":[" + operationTimes(1)
        + ",{\"method\":\"GET\",\"url\":\"http://0:0:0:0:0:0:0:1%0:8080/home\",\"headers\":{\"Authorization\":\"Basic "
        + Base64Utils.encodeToString("user:password".getBytes())
        + "\"},\"silent\":true}]}";
    HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);

    assertTrue(422 == response.getStatusLine().getStatusCode());
  }

  @Test
  public void bulkRequestCanNotContainBulkPathAsUrl() throws Exception {
    BulkRequest req = new BulkRequest();
    BulkOperation op = new BulkOperation();
    op.setUrl(bulkPath);
    op.setMethod("GET");
    op.getHeaders().put("Authorization",
        "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
    req.getOperations().add(op);

    HttpEntity entity =
        new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);

    assertTrue(422 == response.getStatusLine().getStatusCode());
  }

}

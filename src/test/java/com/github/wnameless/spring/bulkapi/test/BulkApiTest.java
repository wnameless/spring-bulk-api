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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.spring.bulkapi.BulkOperation;
import com.github.wnameless.spring.bulkapi.BulkRequest;
import com.github.wnameless.spring.bulkapi.BulkResponse;
import com.github.wnameless.spring.bulkapi.BulkResult;
import com.github.wnameless.spring.bulkapi.test.AppConfig.TestURITransformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.sf.rubycollect4j.Ruby;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;

import java.util.HashMap;
import java.util.Map;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class BulkApiTest {

    @LocalServerPort
    int port;

    @Value("${spring.bulk.api.path:/bulk}")
    String bulkPath;

    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ApplicationContext appCtx;

    String authHeader =
            "Basic " + Base64Utils.encodeToString("user:password".getBytes());

    @Autowired
    TestURITransformer testUriTransformer;

    @Before
    public void setUp() {
        post = new HttpPost("http://localhost:" + port + bulkPath);
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
    }

    private BulkRequest operationTimes(int times) {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();

        times--;

        op.setMethod("GET");
        op.setUrl("home");
        op.getHeaders().put("Authorization", authHeader);
        req.getOperations().add(op);

        while (times > 0) {
            op = new BulkOperation();
            op.setMethod("GET");
            op.setUrl("/home");
            op.getHeaders().put("Authorization", authHeader);
            req.getOperations().add(op);

            times--;
        }

        return req;
    }

    @Test
    public void testBatch() throws Exception {
        HttpEntity entity = new ByteArrayEntity(
                mapper.writeValueAsString(operationTimes(1000)).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        BulkResponse res = new Gson().getAdapter(new TypeToken<BulkResponse>() {
        })
                .fromJson(result);

        assertTrue(200 == Double.valueOf(res.getResults().get(0).getStatus()));
        assertEquals(1000, res.getResults().size());
    }

    @Test
    public void testOverLimitationError() throws Exception {
        HttpEntity entity = new ByteArrayEntity(
                mapper.writeValueAsString(operationTimes(1001)).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertEquals(413, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testSilentMode() throws Exception {
        BulkRequest req = operationTimes(1);
        BulkOperation op = new BulkOperation();
        op.setMethod("GET");
        op.setUrl("/home");
        op.getHeaders().put("Authorization", authHeader);
        op.setSilent(true);
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        BulkResponse res = new Gson().getAdapter(new TypeToken<BulkResponse>() {
        })
                .fromJson(result);

        assertEquals(1, res.getResults().size());
    }

    @Test
    public void testInvalidUrl() throws Exception {
        BulkRequest req = operationTimes(1);
        BulkOperation op = new BulkOperation();
        op.setMethod("GET");
        op.setUrl("http://0:0:0:0:0:0:0:1%0:8080/home");
        op.getHeaders().put("Authorization", authHeader);
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
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

    @Test
    public void bulkRequestToComplexMapping() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("/home2/AAA/ccc");
        op.setMethod("PUT");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(200 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void bulkRequestToWrongMapping() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("/home2/BBB/ccc");
        op.setMethod("PUT");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(422 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void bulkRequestToNonBulkableMapping() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("home3");
        op.setMethod("PUT");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(422 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void bulkRequestWithParam() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("/home4");
        op.setMethod("POST");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        op.getParams().put("abc", "abc");
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(200 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void bulkGetRequestWithParam() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        String queryParamValue = "query_param";
        op.setUrl("/getwithparameter?abc=" + queryParamValue);
        op.setMethod("GET");
        op.setPayload("requestNo:1");
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        String responseString = new BasicResponseHandler().handleResponse(response);
        System.out.println(responseString);

        //region Check if status first request of bulk is 200 or 400(in case of not to having query param)
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(responseString);
        JSONArray slideContent = (JSONArray)json.get("results");
        JSONObject result0 = (JSONObject) slideContent.get(0);
        //endregion

        assertTrue((Long)result0.get("status")==200);
    }

    @Test
    public void bulkRequestByRequestBodyWithNestedObject() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("/list");
        op.setMethod("POST");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        Map<String, Object> params = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("a", Ruby.Hash.of("c", "D").toMap());
                put("b", "E");
            }
        };
        op.setParams(params);
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(200 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void bulkRequestByRequestBodyWithObject() throws Exception {
        BulkRequest req = new BulkRequest();
        BulkOperation op = new BulkOperation();
        op.setUrl("list2");
        op.setMethod("POST");
        op.getHeaders().put("Authorization",
                "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
        Map<String, Object> params = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("a", "D");
                put("b", "E");
            }
        };
        op.setParams(params);
        req.getOperations().add(op);

        HttpEntity entity =
                new ByteArrayEntity(mapper.writeValueAsString(req).getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        assertTrue(200 == response.getStatusLine().getStatusCode());
    }

    @Test
    public void testURITransformer() {
        assertTrue(testUriTransformer.isUsed());
    }

}

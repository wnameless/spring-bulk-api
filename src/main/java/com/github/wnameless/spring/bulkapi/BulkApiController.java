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
package com.github.wnameless.spring.bulkapi;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * {@link BulkApiController} handles the bulk request from API users.
 *
 */
@RestController
public class BulkApiController {

  @Autowired
  Environment env;

  /**
   * Processes bulk requests from API users. Returns a {@link BulkResponse}
   * which contains all the results of the {@link BulkRequest}.
   * 
   * @param req
   *          a {@link BulkRequest}
   * @param servReq
   *          the {@link HttpServletRequest}
   * @return a {@link BulkResponse}
   * @throws BulkApiException
   *           if this bulk request is invalid
   */
  @RequestMapping(value = "${spring.bulk.api.path:/bulk}", method = POST)
  BulkResponse bulk(@RequestBody BulkRequest req, HttpServletRequest servReq) {
    validateBulkRequest(req, servReq);

    List<BulkResult> results = new ArrayList<BulkResult>();
    RestTemplate template = new RestTemplate();
    for (BulkOperation op : req.getOperations()) {
      BodyBuilder bodyBuilder = RequestEntity.method(//
          httpMethod(op.getMethod()), computeUri(servReq, op));

      setHeaders(bodyBuilder, op);
      setBody(bodyBuilder, op);

      ResponseEntity<String> rawRes =
          template.exchange(bodyBuilder.build(), String.class);

      if (!op.isSilent()) results.add(buldResult(rawRes));
    }

    return new BulkResponse(results);
  }

  private void setBody(BodyBuilder bodyBuilder, BulkOperation op) {
    MultiValueMap<String, Object> params =
        new LinkedMultiValueMap<String, Object>();
    for (Entry<String, ?> param : op.getParams().entrySet()) {
      params.add(param.getKey(), param.getValue());
    }
    bodyBuilder.body(params);
  }

  private void setHeaders(BodyBuilder bodyBuilder, BulkOperation op) {
    for (Entry<String, String> header : op.getHeaders().entrySet()) {
      bodyBuilder.header(header.getKey(), header.getValue());
    }
  }

  private URI computeUri(HttpServletRequest servReq, BulkOperation op) {
    URI uri;
    try {
      String rawUrl = servReq.getRequestURL().toString();
      String rawUri = servReq.getRequestURI().toString();
      String servletPath = rawUrl.substring(0, rawUrl.indexOf(rawUri));
      if (op.getUrl().startsWith("/")) {
        uri = new URI(servletPath + op.getUrl());
      } else {
        uri = new URI(servletPath + "/" + op.getUrl());
      }
    } catch (URISyntaxException e) {
      throw new BulkApiException(HttpStatus.UNPROCESSABLE_ENTITY,
          "Invalid URL(" + op.getUrl() + ") exists in this bulk request");
    }

    return uri;
  }

  private BulkResult buldResult(ResponseEntity<String> rawRes) {
    BulkResult res = new BulkResult();
    res.setStatus(Short.valueOf(rawRes.getStatusCode().toString()));
    res.setHeaders(rawRes.getHeaders().toSingleValueMap());
    res.setBody(rawRes.getBody());

    return res;
  }

  private void validateBulkRequest(BulkRequest req,
      HttpServletRequest servReq) {
    int max = Integer.valueOf(env.getProperty("spring.bulk.api.limit", "100"));
    if (req.getOperations().size() > max) {
      throw new BulkApiException(HttpStatus.PAYLOAD_TOO_LARGE,
          "Bulk operations exceed the limitation(" + max + ")");
    }

    // Check if any invalid URL exists
    for (BulkOperation op : req.getOperations()) {
      computeUri(servReq, op);
    }
  }

  private static HttpMethod httpMethod(String method) {
    try {
      return HttpMethod.valueOf(method.toUpperCase());
    } catch (Exception e) {
      return HttpMethod.GET;
    }
  }

}

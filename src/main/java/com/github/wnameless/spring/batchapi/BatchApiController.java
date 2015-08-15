/**
 *
 * @author Wei-Ming Wu
 *
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
package com.github.wnameless.spring.batchapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BatchApiController {

  @RequestMapping(value = "/batch", method = RequestMethod.POST)
  List<BatchResponse> batch(@RequestBody List<BatchRequest> requests,
      HttpServletRequest servleRequest) throws URISyntaxException, IOException {
    List<BatchResponse> responses = new ArrayList<BatchResponse>();
    RestTemplate template = new RestTemplate();
    for (BatchRequest request : requests) {
      BodyBuilder bodyBuilder =
          RequestEntity.method(httpMethod(request.getMethod()),
              new URI((servleRequest.isSecure() ? "https://" : "http://")
                  + servleRequest.getLocalAddr() + ":"
                  + servleRequest.getLocalPort() + request.getUrl()));

      for (Entry<String, String> header : request.getHeaders().entrySet()) {
        bodyBuilder.header(header.getKey(), header.getValue());
      }

      MultiValueMap<String, Object> params =
          new LinkedMultiValueMap<String, Object>();
      for (Entry<String, ?> param : request.getParams().entrySet()) {
        params.add(param.getKey(), param.getValue());
      }
      bodyBuilder.body(params);

      ResponseEntity<String> rawRes =
          template.exchange(bodyBuilder.build(), String.class);
      BatchResponse res = new BatchResponse();
      res.setStatus(Short.valueOf(rawRes.getStatusCode().toString()));
      res.setHeaders(rawRes.getHeaders().toSingleValueMap());
      res.setBody(rawRes.getBody());
      responses.add(res);
    }

    return responses;
  }

  private static HttpMethod httpMethod(String method) {
    try {
      return HttpMethod.valueOf(method.toUpperCase());
    } catch (Exception e) {
      return HttpMethod.GET;
    }
  }

}

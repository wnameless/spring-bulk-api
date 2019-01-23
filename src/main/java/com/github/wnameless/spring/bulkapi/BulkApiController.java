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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * {@link BulkApiController} handles the bulk request from API users.
 *
 */
@RestController
public class BulkApiController {

  @Autowired
  ApplicationContext appCtx;

  @Autowired(required = false)
  BulkApiService bulkApiService;

  private BulkApiService bulkApiService() {
    if (bulkApiService == null)
      bulkApiService = new DefaultBulkApiService(appCtx);

    return bulkApiService;
  }

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
    return bulkApiService().bulk(req, servReq);
  }

}

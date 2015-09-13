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

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * {@link BulkApiExceptionHandlerAdvice} handles all {@link BulkApiException}
 * during a bulk request.
 *
 */
@ControllerAdvice(assignableTypes = BulkApiController.class)
public class BulkApiExceptionHandlerAdvice {

  /**
   * Sets the proper HTTP status code and returns the error message.
   * 
   * @param servRes
   *          a {@link HttpServletResponse}
   * @param exception
   *          a {@link BulkApiException}
   * @return an error message
   */
  @ExceptionHandler(BulkApiException.class)
  @ResponseBody
  String handleError(HttpServletResponse servRes, BulkApiException exception) {
    servRes.setStatus(exception.getStatus().value());
    return exception.getError();
  }

}

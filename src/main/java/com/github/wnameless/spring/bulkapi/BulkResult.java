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

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/**
 * 
 * {@link BulkResult} contains all details of a RESTful operation outcome.
 *
 */
public final class BulkResult {

  private int status;
  private String body;
  private Map<String, String> headers;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String payload;

  /**
   * Returns the HTTP status code of a RESTful operation outcome.
   * 
   * @return a HTTP status code
   */
  public int getStatus() {
    return status;
  }

  /**
   * Sets the HTTP status code of a RESTful operation outcome.
   * 
   * @param status
   *          a HTTP status code
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Returns the HTTP response body of a RESTful operation outcome.
   * 
   * @return a HTTP response body
   */
  public String getBody() {
    return body;
  }

  /**
   * Sets the HTTP response body of a RESTful operation outcome.
   * 
   * @param body
   *          a HTTP response body
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * Returns headers of a RESTful operation outcome.
   * 
   * @return headers of a RESTful operation outcome
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Sets headers of a RESTful operation outcome.
   * 
   * @param headers
   *          headers of a RESTful operation outcome
   */
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /**
   * Returns payload which client put in request without any change
   * Client can put a string or number to distinguish between returned results
   *
   * @return payload of a RESTful operation
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Sets the payload parameters of this RESTful operation.
   *
   * @param payload
   *          an arbitrary string
   */
  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public int hashCode() {
    int result = 27;
    result = 31 ^ result + status;
    result = 31 ^ result + ((body == null) ? 0 : body.hashCode());
    result = 31 ^ result + ((headers == null) ? 0 : headers.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof BulkResult)) return false;
    BulkResult o = (BulkResult) obj;
    return status == o.status
        && (body == null ? o.body == null : body.equals(o.body))
        && (headers == null ? o.headers == null : headers.equals(o.headers));
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{status=" + status + ", body=" + body
        + ", headers=" + headers + "}";
  }

}

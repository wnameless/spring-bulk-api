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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * {@link BulkOperation} contains all details of a RESTful operation.
 *
 */
public final class BulkOperation {

  private String url;
  private String method = "GET";
  private Map<String, Object> params = new LinkedHashMap<String, Object>();
  private Map<String, String> headers = new LinkedHashMap<String, String>();
  private boolean silent = false;
  private String payload;

  /**
   * Returns the URL of this RESTful operation.
   * 
   * @return a URL string
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the URL of this RESTful operation.
   * 
   * @param url
   *          a URL string
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Returns the HTTP method of this RESTful operation.
   * 
   * @return a HTTP method string
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets the HTTP method of this RESTful operation.
   * 
   * @param method
   *          a HTTP method string
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Returns the HTTP parameters of this RESTful operation.
   * 
   * @return parameters of a RESTful operation
   */
  public Map<String, Object> getParams() {
    return params;
  }

  /**
   * Sets the HTTP parameters of this RESTful operation.
   * 
   * @param params
   *          parameters of a RESTful operation
   */
  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  /**
   * Returns the HTTP headers of this RESTful operation.
   * 
   * @return headers of a RESTful operation
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Sets the HTTP headers of this RESTful operation.
   * 
   * @param headers
   *          headers of a RESTful operation
   */
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /**
   * Returns if this RESTful operation result should be omitted.
   * 
   * @return true if this RESTful operation is silent, false otherwise
   */
  public boolean isSilent() {
    return silent;
  }

  /**
   * Sets if this RESTful operation result should be omitted.
   * 
   * @param silent
   *          true if this RESTful operation is silent, false otherwise
   */
  public void setSilent(boolean silent) {
    this.silent = silent;
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
    result = 31 ^ result + ((url == null) ? 0 : url.hashCode());
    result = 31 ^ result + ((method == null) ? 0 : method.hashCode());
    result = 31 ^ result + ((params == null) ? 0 : params.hashCode());
    result = 31 ^ result + ((headers == null) ? 0 : headers.hashCode());
    result = 31 ^ result + (silent ? 1 : 0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof BulkOperation)) return false;
    BulkOperation o = (BulkOperation) obj;
    return (url == null ? o.url == null : url.equals(o.url))
        && (method == null ? o.method == null : method.equals(o.method))
        && (params == null ? o.params == null : params.equals(o.params))
        && (headers == null ? o.headers == null : headers.equals(o.headers))
        && (silent == o.silent);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{url=" + url + ", method=" + method
        + ", params=" + params + ", headers=" + headers + ", silent=" + silent
        + "}";
  }

}

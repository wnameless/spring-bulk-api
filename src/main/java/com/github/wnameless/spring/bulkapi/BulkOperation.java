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
package com.github.wnameless.spring.bulkapi;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BulkOperation {

  private String url;
  private String method = "GET";
  private Map<String, ?> params = new LinkedHashMap<String, Object>();
  private Map<String, String> headers = new LinkedHashMap<String, String>();
  private boolean silent = false;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Map<String, ?> getParams() {
    return params;
  }

  public void setParams(Map<String, ?> params) {
    this.params = params;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public boolean isSilent() {
    return silent;
  }

  public void setSilent(boolean silent) {
    this.silent = silent;
  }

  @Override
  public int hashCode() {
    int result = 27;

    result = 31 ^ result + ((headers == null) ? 0 : headers.hashCode());
    result = 31 ^ result + ((method == null) ? 0 : method.hashCode());
    result = 31 ^ result + ((params == null) ? 0 : params.hashCode());
    result = 31 ^ result + (silent ? 1 : 0);
    result = 31 ^ result + ((url == null) ? 0 : url.hashCode());

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

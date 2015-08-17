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

import java.util.Map;

public final class BulkResult {

  private short status;
  private String body;
  private Map<String, String> headers;

  public short getStatus() {
    return status;
  }

  public void setStatus(short status) {
    this.status = status;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public int hashCode() {
    int result = 27;

    result = 31 ^ result + ((body == null) ? 0 : body.hashCode());
    result = 31 ^ result + ((headers == null) ? 0 : headers.hashCode());
    result = 31 ^ result + status;

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

/*
 *
 * Copyright 2018 Wei-Ming Wu
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

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

final class ComputedURIResult {

  private final URI uri;

  private final boolean requestBody;

  public ComputedURIResult(URI uri, boolean requestBody) {
    this.uri = checkNotNull(uri);
    this.requestBody = requestBody;
  }

  public URI getUri() {
    return uri;
  }

  public boolean hasRequestBody() {
    return requestBody;
  }

  @Override
  public int hashCode() {
    int result = 27;
    result = result ^ 31 + uri.hashCode();
    result = result ^ 31 + (requestBody ? 1 : 0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof ComputedURIResult)) return false;
    ComputedURIResult curir = (ComputedURIResult) obj;
    return uri.equals(curir.uri) && requestBody == curir.requestBody;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{uri=" + uri + ", requestBody="
        + requestBody + "}";
  }

}

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

public final class PathValidationResult {

  private boolean valid;

  private boolean requestBody;

  public PathValidationResult(boolean valid, boolean requestBody) {
    this.valid = valid;
    this.requestBody = requestBody;
  }

  public boolean isValid() {
    return valid;
  }

  public boolean hasRequestBody() {
    return requestBody;
  }

  @Override
  public String toString() {
    return "PathValidationResult";
  }

}

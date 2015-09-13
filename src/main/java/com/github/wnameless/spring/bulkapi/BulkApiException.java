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

import org.springframework.http.HttpStatus;

/**
 * 
 * {@link BulkApiException} represents all kinds of exceptions during the bulk
 * request.
 *
 */
public final class BulkApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final HttpStatus status;
  private final String error;

  /**
   * Creates a {@link BulkApiException}.
   * 
   * @param status
   *          a {@link HttpStatus}
   * @param error
   *          message
   */
  public BulkApiException(HttpStatus status, String error) {
    if (status == null) throw new NullPointerException();
    if (error == null) throw new NullPointerException();

    this.status = status;
    this.error = error;
  }

  /**
   * Returns the {@link HttpStatus} of this {@link BulkApiException}.
   * 
   * @return a {@link HttpStatus}
   */
  public HttpStatus getStatus() {
    return status;
  }

  /**
   * Returns the error message of this {@link BulkApiException}.
   * 
   * @return an error message
   */
  public String getError() {
    return error;
  }

  @Override
  public int hashCode() {
    int result = 27;
    result = result ^ 31 + status.hashCode();
    result = result ^ 31 + error.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof BulkApiException)) return false;
    BulkApiException ex = (BulkApiException) obj;
    return status.equals(ex.status) && error.equals(ex.error);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{status=" + status + ", error=" + error
        + "}";
  }

}

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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * {@link BulkRequest} contains all bulk operations in one bulk request.
 *
 */
public final class BulkRequest {

  private List<BulkOperation> operations = new ArrayList<BulkOperation>();

  /**
   * Returns all operations in this {@link BulkRequest}.
   * 
   * @return a list of {@link BulkOperation}s
   */
  public List<BulkOperation> getOperations() {
    return operations;
  }

  /**
   * Sets all operations in this {@link BulkRequest}.
   * 
   * @param operations
   *          a list of {@link BulkOperation}s
   */
  public void setOperations(List<BulkOperation> operations) {
    this.operations = operations;
  }

  @Override
  public int hashCode() {
    int result = 27;

    result = 31 ^ result + ((operations == null) ? 0 : operations.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof BulkRequest)) return false;

    BulkRequest o = (BulkRequest) obj;

    return operations == null ? o.operations == null
        : operations.equals(o.operations);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{operations=" + operations + "}";
  }

}

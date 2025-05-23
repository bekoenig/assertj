/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.filter;

public abstract class FilterOperator<T> {

  private static final String COMBINING_OPERATOR_IS_NOT_SUPPORTED = "Combining operator is not supported, but you can use Filters, see filteredOn methods in https://www.javadoc.io/doc/org.assertj/assertj-core/latest/org/assertj/core/api/AbstractIterableAssert.html";
  protected final T filterParameter;

  protected FilterOperator(T filterValue) {
    if (filterValue instanceof FilterOperator<?>) throw new UnsupportedOperationException(COMBINING_OPERATOR_IS_NOT_SUPPORTED);
    this.filterParameter = filterValue;
  }

  public abstract <E> Filters<E> applyOn(Filters<E> filters);

}

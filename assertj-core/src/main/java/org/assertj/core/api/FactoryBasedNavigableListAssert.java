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
package org.assertj.core.api;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 * 
 * @since 2.5.0 / 3.5.0
 */
//@format:off
public class FactoryBasedNavigableListAssert<SELF extends FactoryBasedNavigableListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                             ACTUAL extends List<? extends ELEMENT>, 
                                             ELEMENT, 
                                             ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {

  private final AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

// @format:on

  public FactoryBasedNavigableListAssert(ACTUAL actual, Class<?> selfType,
                                         AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(actual, selfType);
    this.assertFactory = assertFactory;
  }

  @Override
  public ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return assertFactory.createAssert(value).as(description);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    checkArgument(iterable instanceof List, "Expecting %s to be a List", iterable);
    return (SELF) new FactoryBasedNavigableListAssert<>((List<? extends ELEMENT>) iterable,
                                                        FactoryBasedNavigableListAssert.class,
                                                        assertFactory);
  }

}

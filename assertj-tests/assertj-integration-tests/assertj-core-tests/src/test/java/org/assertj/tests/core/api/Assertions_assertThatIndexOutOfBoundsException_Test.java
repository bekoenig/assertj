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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatIndexOutOfBoundsException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatIndexOutOfBoundsException_Test {

  @Test
  void should_pass_when_throw_IndexOutOfBoundsException() {
    assertThatIndexOutOfBoundsException().isThrownBy(codeThrowing(new IndexOutOfBoundsException()));
  }

  @Test
  void should_fail_when_throw_wrong_type() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatIndexOutOfBoundsException().isThrownBy(codeThrowing(new Error()));
    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);
    // THEN
    then(assertionError).hasMessageContainingAll(Error.class.getName(), IndexOutOfBoundsException.class.getName());
  }

  @Test
  void should_fail_when_no_exception_thrown() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatIndexOutOfBoundsException().isThrownBy(() -> {});
    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);
    // THEN
    then(assertionError).hasMessage("%nExpecting code to raise a throwable.".formatted());
  }
}

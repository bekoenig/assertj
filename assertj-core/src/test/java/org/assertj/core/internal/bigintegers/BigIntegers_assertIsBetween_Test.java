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
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigIntegers#assertIsBetween(AssertionInfo, BigInteger, BigInteger, BigInteger)}</code>.
 */
class BigIntegers_assertIsBetween_Test extends BigIntegersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsBetween(someInfo(), null, ZERO, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsBetween(someInfo(), ONE, null, ONE));
  }

  @Test
  void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsBetween(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_pass_if_actual_is_in_range() {
    numbers.assertIsBetween(someInfo(), ONE, ZERO, TEN);
    numbers.assertIsBetween(someInfo(), ONE, ONE, TEN);
    numbers.assertIsBetween(someInfo(), ONE, new BigInteger("1"), TEN);
    numbers.assertIsBetween(someInfo(), ONE, ZERO, new BigInteger("1"));
  }

  @Test
  void should_pass_if_actual_is_equal_to_range_start() {
    numbers.assertIsBetween(someInfo(), ONE, ONE, TEN);
  }

  @Test
  void should_pass_if_actual_is_equal_to_range_end() {
    numbers.assertIsBetween(someInfo(), ONE, ZERO, ONE);
  }

  @Test
  void should_fail_if_actual_is_not_in_range_start() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertIsBetween(info, ONE, new BigInteger("2"), TEN));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeBetween(ONE, new BigInteger("2"), TEN, true, true));
  }

  @Test
  void should_fail_if_actual_is_not_in_range_end() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertIsBetween(info, ONE, ZERO, ZERO));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeBetween(ONE, ZERO, ZERO, true, true));
  }
}

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
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertHasEntrySatisfying(AssertionInfo, Map, Object, Consumer)}</code>.
 *
 * @author Valeriy Vyrva
 */
class Maps_assertHasEntrySatisfyingConsumer_Test extends MapsBaseTest {

  private static final Pattern IS_DIGITS = Pattern.compile("^\\d+$");

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = mapOf(entry("name", "Yoda"), entry("color", "green"), entry(null, null));
  }

  @Test
  void should_pass_if_actual_contains_null_key_with_value_matching_condition() {
    maps.assertHasEntrySatisfying(someInfo(), actual, null, s -> assertThat(s).isNull());
  }

  @Test
  void should_pass_if_actual_contains_key_with_value_matching_condition() {
    maps.assertHasEntrySatisfying(someInfo(), actual, "name", s -> assertThat(s).startsWith("Yo"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertHasEntrySatisfying(someInfo(), null, 8,
                                                                                                   o -> assertThat(o).isNotNull()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contains_key() {
    AssertionInfo info = someInfo();
    String key = "id";

    Throwable error = catchThrowable(() -> maps.assertHasEntrySatisfying(info, actual, key,
                                                                         s -> assertThat(s).containsPattern(IS_DIGITS)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainKeys(actual, newLinkedHashSet(key)));
  }

  @Test
  void should_fail_if_actual_contains_key_with_value_not_matching_condition() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertHasEntrySatisfying(someInfo(), actual, "name",
                                                                                                   s -> assertThat(s).containsPattern(IS_DIGITS)))
                                                   .withMessage(shouldContainPattern("Yoda", IS_DIGITS.pattern()).create());
  }

  @Test
  void should_fail_if_actual_contains_null_key_with_value_does_not_matching_condition() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertHasEntrySatisfying(someInfo(), actual, null,
                                                                                                   s -> assertThat(s).isNotNull()))
                                                   .withMessage(actualIsNull());
  }
}

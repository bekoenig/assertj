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
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.assertj.tests.core.testkit.CharArrays.arrayOf;
import static org.assertj.tests.core.testkit.TestData.someInfo;

import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertEqualsIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsIgnoringCase(someInfo(), null, "Luke"))
                                                   .withMessage(shouldBeEqual(null, "Luke").create());
  }

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsIgnoringCase(someInfo(), "Luke", null))
                                                   .withMessage(shouldBeEqual("Luke", null).create());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_regardless_of_case() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsIgnoringCase(someInfo(), "Yoda", "Luke"))
                                                   .withMessage(shouldBeEqual("Yoda", "Luke").create());
  }

  @Test
  void should_pass_if_both_Strings_are_null() {
    strings.assertEqualsIgnoringCase(someInfo(), null, null);
  }

  @Test
  void should_pass_if_both_Strings_are_the_same() {
    String s = "Yoda";
    strings.assertEqualsIgnoringCase(someInfo(), s, s);
  }

  @Test
  void should_pass_if_both_Strings_are_equal_but_not_same() {
    strings.assertEqualsIgnoringCase(someInfo(), "Yoda", new String(arrayOf('Y', 'o', 'd', 'a')));
  }

  @Test
  void should_pass_if_both_Strings_are_equal_ignoring_case() {
    strings.assertEqualsIgnoringCase(someInfo(), "Yoda", "YODA");
  }

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(),
                                                                                                                                           null,
                                                                                                                                           "Luke"))
                                                   .withMessage(shouldBeEqual(null, "Luke").create());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(),
                                                                                                                                           "Yoda",
                                                                                                                                           "Luke"))
                                                   .withMessage(shouldBeEqual("Yoda", "Luke").create());
  }

  @Test
  void should_pass_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), null, null);
  }

  @Test
  void should_pass_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    String s = "Yoda";
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), s, s);
  }

  @Test
  void should_pass_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), "Yoda", new String(
                                                                                                         arrayOf('Y', 'o', 'd',
                                                                                                                 'a')));
  }

  @Test
  void should_pass_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), "Yoda", "YODA");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    // WHEN/THEN
    strings.assertEqualsIgnoringCase(someInfo(), "Leia", "LEIA");
  }

}

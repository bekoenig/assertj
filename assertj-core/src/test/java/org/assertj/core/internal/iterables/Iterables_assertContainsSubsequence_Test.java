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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContainSubsequence.actualDoesNotHaveEnoughElementsToContainSubsequence;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsSubsequence(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Marcin Mikosik
 */
class Iterables_assertContainsSubsequence_Test extends IterablesBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = list("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_subsequence_without_elements_between() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_elements_between() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_with_duplicate_elements_contains_subsequence() {
    actual = list("Yoda", "Luke", "Yoda", "Obi-Wan");
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Obi-Wan"));
    iterables.assertContainsSubsequence(someInfo(), actual, array("Luke", "Obi-Wan"));
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Yoda"));
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_contains_full_subsequence_even_if_partial_subsequence_is_found_before() {
    // GIVEN
    actual = list("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // WHEN/THEN
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    iterables.assertContainsSubsequence(INFO, actual, array("Yoda", "Luke", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsSubsequence(someInfo(), actual, array());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    Object[] subsequence = null;
    // WHEN
    NullPointerException npe = catchNullPointerException(() -> iterables.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    then(npe).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    Object[] subsequence = {};
    // WHEN
    expectAssertionError(() -> iterables.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualIsNotEmpty(actual));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertContainsSubsequence(INFO, actual, array("Yoda")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual() {
    // GIVEN
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsSubsequence(info, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_subsequence() {
    // GIVEN
    Object[] subsequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsSubsequence(info, actual, subsequence));
    // THEN
    verifyFailureThrownWhenSubsequenceNotFound(info, subsequence, 0);
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    // GIVEN
    Object[] subsequence = { "Luke", "Leia", "Han" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsSubsequence(info, actual, subsequence));
    // THEN
    verifyFailureThrownWhenSubsequenceNotFound(info, subsequence, 2);
  }

  @Test
  void should_fail_if_actual_does_not_have_enough_elements_left_to_contain_subsequence_elements_still_to_be_matched() {
    // GIVEN
    actual = list("Leia", "Luke", "Yoda", "Obi-Wan", "Anakin");
    Object[] subsequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verifyFailureThrownWhenSubsequenceNotFound(info, subsequence, 2);
  }

  private void verifyFailureThrownWhenSubsequenceNotFound(AssertionInfo info, Object[] subsequence, int subsequenceIndex) {
    verify(failures).failure(info, shouldContainSubsequence(actual, subsequence, subsequenceIndex,
                                                            StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual, array("yODa", "leia"));
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual,
                                                                             array("YODA", "luke", "lEIA", "Obi-wan"));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO,
                                                                                                                                        actual,
                                                                                                                                        array("Yoda")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Object[] subsequence = null;
    // WHEN
    NullPointerException npe = catchNullPointerException(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO,
                                                                                                                                        actual,
                                                                                                                                        subsequence));
    // THEN
    then(npe).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Object[] subsequence = {};
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual,
                                                                                                        subsequence));
    // THEN
    verify(failures).failure(INFO, actualIsNotEmpty(actual));
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] subsequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual,
                                                                                                        subsequence));
    // THEN
    verify(failures).failure(INFO, actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] subsequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(info, actual,
                                                                                                        subsequence));
    // THEN
    verify(failures).failure(info, shouldContainSubsequence(actual, subsequence, 0, comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] subsequence = { "Luke", "Leia", "Han" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(info, actual,
                                                                                                        subsequence));
    // THEN
    verify(failures).failure(info, shouldContainSubsequence(actual, subsequence, 2, comparisonStrategy));
  }

}

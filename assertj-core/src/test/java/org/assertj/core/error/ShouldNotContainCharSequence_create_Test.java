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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.description.TextDescription;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class ShouldNotContainCharSequence_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContain("Yoda", "od", StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "not to contain:%n" +
                                   "  \"od\"%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContain("Yoda", "od",
                                                   new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "not to contain:%n" +
                                   "  \"od\"%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_create_error_message_with_several_string_values() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContain("Yoda", array("od", "ya"), set("ya"),
                                                   StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "not to contain:%n" +
                                   "  [\"od\", \"ya\"]%n" +
                                   "but found:%n" +
                                   "  [\"ya\"]%n"));
  }

  @Test
  void should_create_error_message_for_ignoring_case() {
    // GIVEN
    ErrorMessageFactory factory = ShouldNotContainCharSequence.shouldNotContainIgnoringCase("Yoda", "OD");
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "not to contain (ignoring case):%n" +
                                   "  \"OD\"%n"));
  }

  @Test
  void should_create_error_message_for_ignoring_case_with_multiple_findings() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainIgnoringCase("Yoda", array("OD", "da", "Luke"), set("OD", "da"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "not to contain (ignoring case):%n" +
                                   "  [\"OD\", \"da\", \"Luke\"]%n" +
                                   "but found:%n" +
                                   "  [\"OD\", \"da\"]%n"));
  }

}

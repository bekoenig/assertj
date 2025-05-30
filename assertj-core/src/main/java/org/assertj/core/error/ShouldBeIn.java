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

import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a value is in a group of values (e.g. an array or
 * collection) failed.
 *
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldBeIn extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeIn}</code>.
   * @param actual the actual value in the failed assertion.
   * @param values the group of values where {@code actual} is expected to be in.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeIn(Object actual, Object values, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeIn(actual, values, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeIn}</code>.
   * @param actual the actual value in the failed assertion.
   * @param values the group of values where {@code actual} is expected to be in.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeIn(Object actual, Object values) {
    return new ShouldBeIn(actual, values, StandardComparisonStrategy.instance());
  }

  private ShouldBeIn(Object actual, Object values, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be in:%n  %s%n%s", actual, values, comparisonStrategy);
  }

}

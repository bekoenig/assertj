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
package org.assertj.tests.core.api.recursive.fieldlocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Lists.list;

import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_isTopLevelField_Test {

  @ParameterizedTest
  @MethodSource
  void should_return_false_for_root_location_or_nested_field_location(FieldLocation fieldLocation) {
    assertThat(fieldLocation.isTopLevelField()).isFalse();
  }

  private static Stream<FieldLocation> should_return_false_for_root_location_or_nested_field_location() {
    return Stream.of(rootFieldLocation(),
                     new FieldLocation(list("[0]")),
                     new FieldLocation(list("[1]")),
                     new FieldLocation(list("friend", "name")));
  }

  @ParameterizedTest
  @MethodSource
  void should_return_true_for_top_level_field(FieldLocation fieldLocation) {
    assertThat(fieldLocation.isTopLevelField()).isTrue();
  }

  private static Stream<FieldLocation> should_return_true_for_top_level_field() {
    return Stream.of(new FieldLocation(list("name")),
                     new FieldLocation(list("[0]", "name")),
                     new FieldLocation(list("[1]", "name")));
  }

}

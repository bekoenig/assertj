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

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.WEEKS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatTemporal;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.assertj.core.api.BDDAssertions.thenTemporal;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.MethodSource;

class AbstractTemporalAssert_isCloseTo_Test {

  private static final ZoneId NEW_YORK_ZONE = ZoneId.of("America/New_York");

  private static final Instant _2017_Mar_12_07_10_Instant = Instant.parse("2017-03-12T07:10:00.00Z");
  private static final Instant _2017_Mar_12_07_12_Instant = Instant.parse("2017-03-12T07:12:00.00Z");
  private static final Instant _2017_Mar_08_07_10_Instant = Instant.parse("2017-03-08T07:10:00.00Z");
  private static final LocalDate _2017_Mar_10 = LocalDate.of(2017, Month.MARCH, 10);
  private static final LocalDate _2017_Mar_12 = LocalDate.of(2017, Month.MARCH, 12);
  private static final LocalDate _2017_Mar_27 = LocalDate.of(2017, Month.MARCH, 27);
  private static final LocalDate _2017_Mar_08 = LocalDate.of(2017, Month.MARCH, 8);
  private static final LocalTime _07_10 = LocalTime.of(7, 10);
  private static final LocalTime _07_11 = LocalTime.of(7, 11);
  private static final LocalTime _07_12 = LocalTime.of(7, 12);
  private static final LocalTime _07_23 = LocalTime.of(7, 23);
  private static final LocalDateTime _2017_Mar_12_07_10 = LocalDateTime.of(_2017_Mar_12, _07_10);
  private static final LocalDateTime _2017_Mar_12_07_12 = LocalDateTime.of(_2017_Mar_12, _07_12);
  private static final LocalDateTime _2017_Mar_10_07_12 = LocalDateTime.of(_2017_Mar_10, _07_12);
  private static final LocalDateTime _2017_Mar_12_07_23 = LocalDateTime.of(_2017_Mar_12, _07_23);
  private static final LocalDateTime _2017_Mar_08_07_10 = LocalDateTime.of(_2017_Mar_08, _07_10);

  private static final AbstractTemporalAssert<?, ?>[] nullAsserts = {
      assertThat((Instant) null),
      assertThat((Instant) null),
      assertThat((LocalDateTime) null),
      assertThat((LocalDate) null),
      assertThat((LocalTime) null),
      assertThat((OffsetDateTime) null),
      assertThat((ZonedDateTime) null),
      assertThat((OffsetTime) null)
  };

  private static final AbstractTemporalAssert<?, ?>[] temporalAsserts = {
      assertThat(_2017_Mar_12_07_10_Instant),
      assertThat(_2017_Mar_12_07_10_Instant),
      assertThat(_2017_Mar_12_07_10),
      assertThat(_2017_Mar_12),
      assertThat(_07_10),
      assertThat(OffsetDateTime.of(_2017_Mar_12_07_10, UTC)),
      assertThat(ZonedDateTime.of(_2017_Mar_12_07_10, NEW_YORK_ZONE)),
      assertThat(OffsetTime.of(_07_10, UTC))
  };

  private static final Temporal[] closeTemporals = {
      _2017_Mar_12_07_12_Instant,
      _2017_Mar_12_07_10_Instant.plusMillis(1L),
      _2017_Mar_10_07_12,
      _2017_Mar_10,
      _07_12,
      OffsetDateTime.of(_2017_Mar_12_07_12, UTC),
      ZonedDateTime.of(_2017_Mar_10_07_12, NEW_YORK_ZONE),
      OffsetTime.of(_07_12, UTC)
  };

  private static final Temporal[] farTemporals = new Temporal[] {
      _2017_Mar_08_07_10_Instant,
      Instant.MIN,
      _2017_Mar_08_07_10,
      _2017_Mar_27,
      _07_23,
      OffsetDateTime.of(_2017_Mar_12_07_23, UTC),
      ZonedDateTime.of(_2017_Mar_08_07_10, NEW_YORK_ZONE),
      OffsetTime.of(_07_23, UTC)
  };

  private static final String[] differenceMessages = {
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 50 Hours but difference was 96 Hours".formatted(_2017_Mar_12_07_10_Instant,
                                                                                                                _2017_Mar_08_07_10_Instant),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 2 Millis but difference was PT8765837682367H10M".formatted(_2017_Mar_12_07_10_Instant,
                                                                                                                           Instant.MIN),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 50 Hours but difference was 96 Hours".formatted(_2017_Mar_12_07_10,
                                                                                                                _2017_Mar_08_07_10),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 3 Days but difference was 15 Days".formatted(_2017_Mar_12,
                                                                                                             _2017_Mar_27),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 5 Minutes but difference was 13 Minutes".formatted(_07_10,
                                                                                                                   _07_23),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 10 Minutes but difference was 13 Minutes".formatted(OffsetDateTime.of(_2017_Mar_12_07_10,
                                                                                                                                      UTC),
                                                                                                                    OffsetDateTime.of(_2017_Mar_12_07_23,
                                                                                                                                      UTC)),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nby less than 95 Hours but difference was 95 Hours".formatted(ZonedDateTime.of(_2017_Mar_12_07_10,
                                                                                                                                       NEW_YORK_ZONE),
                                                                                                                      ZonedDateTime.of(_2017_Mar_08_07_10,
                                                                                                                                       NEW_YORK_ZONE)),
      "%nExpecting actual:%n  %s%nto be close to:%n  %s%nwithin 2 Minutes but difference was 13 Minutes".formatted(OffsetTime.of(_07_10,
                                                                                                                                 UTC),
                                                                                                                   OffsetTime.of(_07_23,
                                                                                                                                 UTC)),
  };

  private static final TemporalUnitOffset[] offsets = {
      within(50, HOURS),
      within(2, MILLIS),
      within(50, HOURS),
      within(3, DAYS),
      within(5, MINUTES),
      within(10, MINUTES),
      byLessThan(95, HOURS),
      within(2, MINUTES)
  };

  private static final TemporalUnitOffset[] inapplicableOffsets = { null, null, null, within(1, MINUTES),
      within(1, DAYS), null, null, within(1, WEEKS) };

  static Object[][] parameters() {
    DateTimeFormatter[] formatters = {
        ISO_INSTANT,
        ISO_INSTANT,
        ISO_LOCAL_DATE_TIME,
        ISO_LOCAL_DATE,
        ISO_LOCAL_TIME,
        ISO_OFFSET_DATE_TIME,
        ISO_ZONED_DATE_TIME,
        ISO_TIME
    };

    int assertsLength = nullAsserts.length; // same as temporalAsserts.length
    Object[][] parameters = new Object[assertsLength][9];
    for (int i = 0; i < assertsLength; i++) {
      parameters[i][0] = nullAsserts[i];
      parameters[i][1] = temporalAsserts[i];
      parameters[i][2] = offsets[i];
      parameters[i][3] = closeTemporals[i];
      parameters[i][4] = formatters[i].format(closeTemporals[i]);
      parameters[i][5] = farTemporals[i];
      parameters[i][6] = formatters[i].format(farTemporals[i]);
      parameters[i][7] = differenceMessages[i];
      parameters[i][8] = inapplicableOffsets[i];
    }
    return parameters;
  }

  @SuppressWarnings("unchecked")
  private static AbstractTemporalAssert<?, Temporal> nullAssert(ArgumentsAccessor arguments) {
    return arguments.get(0, AbstractTemporalAssert.class);
  }

  @SuppressWarnings("unchecked")
  private static AbstractTemporalAssert<?, Temporal> temporalAssert(ArgumentsAccessor arguments) {
    return arguments.get(1, AbstractTemporalAssert.class);
  }

  private static TemporalUnitOffset offset(ArgumentsAccessor arguments) {
    return arguments.get(2, TemporalUnitOffset.class);
  }

  private static Temporal closeTemporal(ArgumentsAccessor arguments) {
    return arguments.get(3, Temporal.class);
  }

  private static String closeTemporalAsString(ArgumentsAccessor arguments) {
    return arguments.getString(4);
  }

  private static Temporal farTemporal(ArgumentsAccessor arguments) {
    return arguments.get(5, Temporal.class);
  }

  private static String farTemporalAsString(ArgumentsAccessor arguments) {
    return arguments.getString(6);
  }

  private static String differenceMessage(ArgumentsAccessor arguments) {
    return arguments.getString(7);
  }

  private static TemporalUnitOffset inapplicableOffset(ArgumentsAccessor arguments) {
    return arguments.get(8, TemporalUnitOffset.class);
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_if_actual_is_null(ArgumentsAccessor args) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> nullAssert(args).isCloseTo(closeTemporal(args), offset(args)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_if_temporal_parameter_is_null(ArgumentsAccessor args) {
    assertThatNullPointerException().isThrownBy(() -> temporalAssert(args).isCloseTo((Temporal) null, offset(args)))
                                    .withMessage("The temporal object to compare actual with should not be null");
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_if_temporal_parameter_as_string_is_null(ArgumentsAccessor args) {
    assertThatNullPointerException().isThrownBy(() -> temporalAssert(args).isCloseTo((String) null, offset(args)))
                                    .withMessage("The String representing of the temporal object to compare actual with should not be null");
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_if_offset_parameter_is_null(ArgumentsAccessor args) {
    assertThatNullPointerException().isThrownBy(() -> temporalAssert(args).isCloseTo(closeTemporal(args), null))
                                    .withMessage("The offset should not be null");
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_when_offset_is_inapplicable(ArgumentsAccessor args) {
    // GIVEN
    TemporalUnitOffset inapplicableOffset = inapplicableOffset(args);
    // WHEN
    ThrowingCallable code = () -> temporalAssert(args).isCloseTo(closeTemporal(args), inapplicableOffset);
    // THEN
    if (inapplicableOffset != null) {
      thenExceptionOfType(UnsupportedTemporalTypeException.class).isThrownBy(code)
                                                                 .withMessage("Unsupported unit: "
                                                                              + inapplicableOffset.getUnit());
    }
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_pass_when_within_offset(ArgumentsAccessor args) {
    temporalAssert(args).isCloseTo(closeTemporal(args), offset(args));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_pass_when_temporal_passed_as_string_is_within_offset(ArgumentsAccessor args) {
    temporalAssert(args).isCloseTo(closeTemporalAsString(args), offset(args));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_outside_offset(ArgumentsAccessor args) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> temporalAssert(args).isCloseTo(farTemporal(args), offset(args)));
    // THEN
    then(assertionError).hasMessage(differenceMessage(args));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_fail_when_temporal_passed_as_string_is_outside_offset(ArgumentsAccessor args) {
    // WHEN
    AssertionError error = expectAssertionError(() -> temporalAssert(args).isCloseTo(farTemporalAsString(args), offset(args)));
    // THEN
    then(error).hasMessage(differenceMessage(args));
  }

  @ParameterizedTest
  @MethodSource
  void should_support_base_temporal_type_assertions(Temporal now) {
    assertThatTemporal(now).isCloseTo(now, within(1, ChronoUnit.SECONDS));
    thenTemporal(now).isCloseTo(now, within(1, ChronoUnit.SECONDS));
  }

  static Stream<Temporal> should_support_base_temporal_type_assertions() {
    return Stream.of(Instant.now(), ZonedDateTime.now(), OffsetDateTime.now());
  }

  @ParameterizedTest
  @MethodSource("within_duration")
  void should_support_within_duration(LocalTime closeTime) {
    assertThat(_07_10).isCloseTo(closeTime, within(Duration.ofMinutes(2)));
  }

  static Stream<LocalTime> within_duration() {
    return Stream.of(_07_10, _07_11, _07_12.minusNanos(1), _07_12);
  }

  @Test
  void should_fail_when_not_within_duration() {
    // GIVEN
    LocalTime actual = _07_10;
    LocalTime other = _07_12;
    TemporalUnitOffset offset = within(Duration.ofMinutes(1));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isCloseTo(other, offset));
    // THEN
    then(error).hasMessage(shouldBeCloseTo(actual, other, offset.getBeyondOffsetDifferenceDescription(actual, other)).create());
  }

  @ParameterizedTest
  @MethodSource("by_less_than_duration")
  void should_support_less_than_with_duration(LocalTime closeTime) {
    assertThat(_07_10).isCloseTo(closeTime, byLessThan(Duration.ofMinutes(2)));
  }

  static Stream<LocalTime> by_less_than_duration() {
    return Stream.of(_07_10, _07_11, _07_12.minusNanos(1));
  }

  @Test
  void should_fail_when_not_less_than_duration() {
    // GIVEN
    LocalTime actual = _07_10;
    LocalTime other = _07_12;
    TemporalUnitOffset offset = byLessThan(Duration.ofMinutes(2));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isCloseTo(other, offset));
    // THEN
    then(error).hasMessage(shouldBeCloseTo(actual, other, offset.getBeyondOffsetDifferenceDescription(actual, other)).create());
  }

}

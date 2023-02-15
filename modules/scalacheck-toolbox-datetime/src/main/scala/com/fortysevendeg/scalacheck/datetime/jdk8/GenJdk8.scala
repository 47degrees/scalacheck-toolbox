/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.scalacheck.datetime.jdk8

import scala.jdk.CollectionConverters._

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import java.time._
import java.time.temporal.ChronoUnit.MILLIS

import com.fortysevendeg.scalacheck.datetime.{Granularity, YearRange}

trait GenJdk8 {

  def genZonedDateTimeWithZone(
      maybeZone: Option[ZoneId]
  )(implicit yearRange: YearRange): Gen[ZonedDateTime] =
    for {
      year  <- Gen.choose(yearRange.min, yearRange.max)
      month <- Gen.choose(1, 12)
      maxDaysInMonth = Month.of(month).length(Year.of(year).isLeap)
      dayOfMonth   <- Gen.choose(1, maxDaysInMonth)
      hour         <- Gen.choose(0, 23)
      minute       <- Gen.choose(0, 59)
      second       <- Gen.choose(0, 59)
      nanoOfSecond <- Gen.choose(0, 999999999)
      zoneId <-
        maybeZone
          .map(Gen.const)
          .getOrElse(Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toList).map(ZoneId.of))
    } yield ZonedDateTime
      .of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, zoneId)

  def genZonedDateTime(implicit
      granularity: Granularity[ZonedDateTime],
      yearRange: YearRange
  ): Gen[ZonedDateTime] =
    genZonedDateTimeWithZone(None).map(granularity.normalize)

  def genDurationOf(minMillis: Long, maxMillis: Long) =
    Gen.choose(minMillis, maxMillis).map(Duration.of(_, MILLIS))

  val genDuration: Gen[Duration] = genDurationOf(Long.MinValue, Long.MaxValue / 1000)
}

object GenJdk8 extends GenJdk8

object ArbitraryJdk8 extends GenJdk8 {

  private[this] val utcZoneId: ZoneId = ZoneId.of("UTC")

  implicit def arbZonedDateTimeJdk8(implicit
      granularity: Granularity[ZonedDateTime],
      yearRange: YearRange
  ): Arbitrary[ZonedDateTime] =
    Arbitrary(genZonedDateTime.map(granularity.normalize))

  implicit def arbLocalDateTimeJdk8(implicit
      granularity: Granularity[ZonedDateTime],
      yearRange: YearRange
  ): Arbitrary[LocalDateTime] =
    Arbitrary(
      genZonedDateTimeWithZone(Some(utcZoneId)).map(granularity.normalize).map(_.toLocalDateTime)
    )

  implicit def arbLocalDateJdk8(implicit
      granularity: Granularity[ZonedDateTime],
      yearRange: YearRange
  ): Arbitrary[LocalDate] =
    Arbitrary(
      genZonedDateTimeWithZone(Some(utcZoneId)).map(granularity.normalize).map(_.toLocalDate)
    )

  implicit def arbInstantJdk8(implicit
      granularity: Granularity[ZonedDateTime],
      yearRange: YearRange
  ): Arbitrary[Instant] =
    Arbitrary(genZonedDateTimeWithZone(Some(utcZoneId)).map(granularity.normalize).map(_.toInstant))
}

/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package com.fortysevendeg.scalacheck.datetime.instances

import com.fortysevendeg.scalacheck.datetime.typeclasses._
import java.time._
import java.time.temporal.ChronoUnit.MILLIS

trait Jdk8Instances {

  implicit val jdkForDurationZonedDateTime: ScalaCheckDateTimeInfra[ZonedDateTime, Duration] =
    new ScalaCheckDateTimeInfra[ZonedDateTime, Duration] {
      def addRange(zonedDateTime: ZonedDateTime, duration: Duration): ZonedDateTime =
        zonedDateTime.plus(duration)
      def addMillis(zonedDateTime: ZonedDateTime, millis: Long): ZonedDateTime =
        zonedDateTime.plus(millis, MILLIS)
      def getMillis(zonedDateTime: ZonedDateTime): Long             = zonedDateTime.toInstant.toEpochMilli
      def isBefore(dt1: ZonedDateTime, dt2: ZonedDateTime): Boolean = dt1.isBefore(dt2)
    }

  implicit val jdkForDurationLocalDateTime: ScalaCheckDateTimeInfra[LocalDateTime, Duration] =
    new ScalaCheckDateTimeInfra[LocalDateTime, Duration] {
      def addRange(localDateTime: LocalDateTime, duration: Duration): LocalDateTime =
        localDateTime.plus(duration)
      def addMillis(localDateTime: LocalDateTime, millis: Long): LocalDateTime =
        localDateTime.plus(millis, MILLIS)
      def getMillis(localDateTime: LocalDateTime): Long =
        localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli
      def isBefore(dt1: LocalDateTime, dt2: LocalDateTime): Boolean = dt1.isBefore(dt2)
    }

  implicit val jdkForDurationLocalDate: ScalaCheckDateInfra[LocalDate, Period] =
    new ScalaCheckDateInfra[LocalDate, Period] {
      override def addRange(date: LocalDate, range: Period): LocalDate = date.plus(range)
      override def getDiffDays(from: LocalDate, to: LocalDate): Int =
        Period.between(from, to).getDays
      override def addDays(dateTime: LocalDate, days: Int): LocalDate =
        dateTime.plusDays(days.toLong)
      override def isBefore(d1: LocalDate, d2: LocalDate): Boolean = d1.isBefore(d2)
    }

  implicit val jdkForDurationTime: ScalaCheckTimeInfra[LocalTime] =
    new ScalaCheckTimeInfra[LocalTime] {
      def isBefore(t1: LocalTime, t2: LocalTime): Boolean = t1.isBefore(t2)
      def diffMillis(lower: LocalTime, higher: LocalTime): Long =
        Duration.between(lower, higher).toMillis
      def addMillis(d: LocalTime, millis: Long): LocalTime = d.plus(Duration.ofMillis(millis))
    }

  implicit val jdkForDurationInstant: ScalaCheckDateTimeInfra[Instant, Duration] =
    new ScalaCheckDateTimeInfra[Instant, Duration] {
      def addRange(Instant: Instant, duration: Duration): Instant = Instant.plus(duration)
      def addMillis(Instant: Instant, millis: Long): Instant      = Instant.plus(millis, MILLIS)
      def getMillis(Instant: Instant): Long                       = Instant.toEpochMilli
      def isBefore(dt1: Instant, dt2: Instant): Boolean           = dt1.isBefore(dt2)
    }
}

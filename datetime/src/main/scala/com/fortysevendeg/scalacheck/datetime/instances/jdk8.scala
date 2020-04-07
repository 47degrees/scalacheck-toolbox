/*
 * Copyright 2016-2020 47 Degrees <http://47deg.com>
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

  implicit val jdk8ForDuration: ScalaCheckDateTimeInfra[ZonedDateTime, Duration] =
    new ScalaCheckDateTimeInfra[ZonedDateTime, Duration] {
      def addRange(zonedDateTime: ZonedDateTime, duration: Duration): ZonedDateTime =
        zonedDateTime.plus(duration)
      def addMillis(zonedDateTime: ZonedDateTime, millis: Long): ZonedDateTime =
        zonedDateTime.plus(millis, MILLIS)
      def getMillis(zonedDateTime: ZonedDateTime): Long =
        zonedDateTime.toInstant.toEpochMilli
    }

  implicit val jdk8LocalDateTime: ScalaCheckDateTimeInfra[LocalDateTime, Duration] =
    jdk8ForDurationFrom[LocalDateTime](_.toLocalDateTime, _.atZone(ZoneOffset.UTC))

  implicit val jdk8Instant: ScalaCheckDateTimeInfra[Instant, Duration] =
    jdk8ForDurationFrom[Instant](_.toInstant, _.atZone(ZoneOffset.UTC))

  private[this] def jdk8ForDurationFrom[A](
      fromZDT: ZonedDateTime => A,
      toZDT: A => ZonedDateTime
  ): ScalaCheckDateTimeInfra[A, Duration] =
    new ScalaCheckDateTimeInfra[A, Duration] {
      def addRange(dateTime: A, range: Duration): A =
        fromZDT(jdk8ForDuration.addRange(toZDT(dateTime), range))
      def addMillis(dateTime: A, millis: Long): A =
        fromZDT(jdk8ForDuration.addMillis(toZDT(dateTime), millis))
      override def getMillis(dateTime: A): Long =
        jdk8ForDuration.getMillis(toZDT(dateTime))
    }
}

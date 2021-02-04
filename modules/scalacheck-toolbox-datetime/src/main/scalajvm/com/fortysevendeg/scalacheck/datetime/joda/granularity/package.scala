/*
 * Copyright 2016-2021 47 Degrees Open Source <https://www.47deg.com>
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

package com.fortysevendeg.scalacheck.datetime.joda

import com.fortysevendeg.scalacheck.datetime.Granularity
import org.joda.time.DateTime

package object granularity {

  implicit val seconds: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize   = (dt: DateTime) => dt.withMillisOfSecond(0)
    val description = "Seconds"
  }

  implicit val minutes: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize   = (dt: DateTime) => dt.withMillisOfSecond(0).withSecondOfMinute(0)
    val description = "Minutes"
  }

  implicit val hours: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) =>
      dt.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0)
    val description = "Hours"
  }

  implicit val days: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) =>
      dt.withMillisOfSecond(0)
        .withSecondOfMinute(0)
        .withMinuteOfHour(0)
        .withHourOfDay(0)
    val description = "Days"
  }

  implicit val years: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) =>
      dt.withMillisOfSecond(0)
        .withSecondOfMinute(0)
        .withMinuteOfHour(0)
        .withHourOfDay(0)
        .withDayOfYear(1)
    val description = "Years"
  }
}

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

package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.ZonedDateTime

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
 * Created by timpigden on 15/02/17.
 * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
 */
object ZonedDateTimeGranularity {

  case class ZonedDateTimeGranularity(
      description: String,
      normalize: (ZonedDateTime) => ZonedDateTime)
      extends Granularity[ZonedDateTime]

  val seconds = ZonedDateTimeGranularity("Seconds", _.withNano(0))
  val minutes = ZonedDateTimeGranularity("Minutes", _.withNano(0).withSecond(0))
  val hours   = ZonedDateTimeGranularity("Hours", _.withNano(0).withSecond(0).withMinute(0))
  val days    = ZonedDateTimeGranularity("Days", _.withNano(0).withSecond(0).withMinute(0).withHour(0))

  // Set the day of year before the hour as some days (very very rarely) start at 1am.
  // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
  // So Java 8 sets the hour to 1am.
  // If you then set the day of year to Jan 1, and that day starts at 12am,
  // then the granularity has been set wrong in that case. Insane.
  val years = ZonedDateTimeGranularity(
    "Years",
    _.withDayOfYear(1).withNano(0).withSecond(0).withMinute(0).withHour(0))

}

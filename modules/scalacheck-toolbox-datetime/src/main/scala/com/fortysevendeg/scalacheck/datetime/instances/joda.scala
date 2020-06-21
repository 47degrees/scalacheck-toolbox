/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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
import org.joda.time._

trait JodaInstances {

  // todo have another instance with Duration rather than period?
  implicit val jodaForPeriod: ScalaCheckDateTimeInfra[DateTime, Period] =
    new ScalaCheckDateTimeInfra[DateTime, Period] {
      def addRange(dateTime: DateTime, period: Period): DateTime =
        dateTime.plus(period)
      def addMillis(dateTime: DateTime, millis: Long): DateTime =
        dateTime.plus(millis)
      def getMillis(dateTime: DateTime): Long = dateTime.getMillis
    }
}

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

package com.fortysevendeg.scalacheck.datetime

trait YearRange {
  val min: Int
  val max: Int
}

object YearRange {

  /**
   * The default range of years. Set to a safe subset shared by both `java.time` and
   * `org.joda.time`.
   */
  implicit val default: YearRange = new YearRange {
    val min: Int = -292275054
    val max: Int = 292278993
  }

  /** Defines a year range between 1970 and the max year you specify. */
  def epochTo(year: Int) = new YearRange {
    val min = 1970
    val max = year
  }

  /** Defines a year range between your own defined min and max years. */
  def between(minYear: Int, maxYear: Int) = new YearRange {
    val min = minYear
    val max = maxYear
  }
}

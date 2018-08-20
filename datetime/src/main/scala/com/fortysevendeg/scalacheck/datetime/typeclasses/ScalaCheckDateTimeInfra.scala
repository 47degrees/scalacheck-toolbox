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

package com.fortysevendeg.scalacheck.datetime.typeclasses

/*
 * TODO:
 *  - Use simulacrum?
 *  - Rename this
 *  - Try the Aux pattern to remove the range type param?
 */
trait ScalaCheckDateTimeInfra[D, R] {
  def addRange(dateTime: D, range: R): D
  def addMillis(dateTime: D, millis: Long): D
  def getMillis(dateTime: D): Long
  def isBefore(dt1: D, dt2: D): Boolean
}

trait ScalaCheckDateInfra[D, R] {
  def addRange(dateTime: D, range: R): D
  def getDiffDays(from: D, to: D): Int
  def addDays(dateTime: D, days: Int): D
  def isBefore(d1: D, d2: D): Boolean
}

trait ScalaCheckTimeInfra[D] {
  def isBefore(t1: D, t2: D): Boolean
  def diffMillis(lower: D, higher: D): Long
  def addMillis(d: D, millis: Long): D
}

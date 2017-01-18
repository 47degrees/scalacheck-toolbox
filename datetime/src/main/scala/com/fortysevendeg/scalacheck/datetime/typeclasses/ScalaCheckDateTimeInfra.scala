/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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
}

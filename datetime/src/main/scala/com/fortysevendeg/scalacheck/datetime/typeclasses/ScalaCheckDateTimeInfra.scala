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



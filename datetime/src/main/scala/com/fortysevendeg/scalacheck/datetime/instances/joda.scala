/*
 * scalacheck-datetime
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.datetime.instances

import com.fortysevendeg.scalacheck.datetime.typeclasses._
import org.joda.time._

trait JodaInstances {

  // todo have another instance with Duration rather than period?
  implicit val jodaForPeriod: ScalaCheckDateTimeInfra[DateTime, Period] = new ScalaCheckDateTimeInfra[DateTime, Period] {
    def addRange(dateTime: DateTime, period: Period): DateTime = dateTime.plus(period)
    def addMillis(dateTime: DateTime, millis: Long): DateTime =  dateTime.plus(millis)
    def getMillis(dateTime: DateTime): Long = dateTime.getMillis
  }
}

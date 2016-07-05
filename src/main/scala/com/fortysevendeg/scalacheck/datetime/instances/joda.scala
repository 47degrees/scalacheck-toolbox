package com.fortysevendeg.scalacheck.datetime.instances

import com.fortysevendeg.scalacheck.datetime.typeclasses._
import org.joda.time._

trait JodaInstances {

  // todo have another instance with Duration rather than period?
  implicit val dateTime: ScalaCheckDateTimeInfra[DateTime, Period] = new ScalaCheckDateTimeInfra[DateTime, Period] {
    type RangeForA = Period
    def addRange(dateTime: DateTime, period: Period): DateTime = dateTime.plus(period)
    def addMillis(dateTime: DateTime, millis: Long): DateTime =  dateTime.plus(millis)
    def getMillis(dateTime: DateTime): Long = dateTime.getMillis
  }
}

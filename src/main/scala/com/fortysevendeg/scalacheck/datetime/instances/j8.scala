package com.fortysevendeg.scalacheck.datetime.instances

import com.fortysevendeg.scalacheck.datetime.typeclasses._
import java.time._
import java.time.temporal.ChronoUnit.MILLIS

trait J8Instances {

  implicit val j8ForDuration: ScalaCheckDateTimeInfra[ZonedDateTime, Duration] = new ScalaCheckDateTimeInfra[ZonedDateTime, Duration] {
    def addRange(zonedDateTime: ZonedDateTime, duration: Duration): ZonedDateTime = zonedDateTime.plus(duration)
    def addMillis(zonedDateTime: ZonedDateTime, millis: Long): ZonedDateTime = zonedDateTime.plus(millis, MILLIS)
    def getMillis(zonedDateTime: ZonedDateTime): Long = zonedDateTime.toInstant.toEpochMilli
  }

}

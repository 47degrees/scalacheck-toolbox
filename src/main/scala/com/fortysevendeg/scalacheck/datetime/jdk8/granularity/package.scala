package com.fortysevendeg.scalacheck.datetime.jdk8

import com.fortysevendeg.scalacheck.datetime.Granularity
import java.time.ZonedDateTime

package object granularity {

  implicit val seconds: Granularity[ZonedDateTime] = new Granularity[ZonedDateTime] {
    val normalize = (dt: ZonedDateTime) => dt.withNano(0)
    val description = "Seconds"
  }

  implicit val minutes: Granularity[ZonedDateTime] = new Granularity[ZonedDateTime] {
    val normalize = (dt: ZonedDateTime) => dt.withNano(0).withSecond(0)
    val description = "Minutes"
  }

  implicit val hours: Granularity[ZonedDateTime] = new Granularity[ZonedDateTime] {
    val normalize = (dt: ZonedDateTime) => dt.withNano(0).withSecond(0).withMinute(0)
    val description = "Hours"
  }

  implicit val days: Granularity[ZonedDateTime] = new Granularity[ZonedDateTime] {
    val normalize = (dt: ZonedDateTime) => dt.withNano(0).withSecond(0).withMinute(0).withHour(0)
    val description = "Days"
  }

  implicit val years: Granularity[ZonedDateTime] = new Granularity[ZonedDateTime] {
    val normalize = (dt: ZonedDateTime) => dt.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfYear(1)
    val description = "Years"
  }
}

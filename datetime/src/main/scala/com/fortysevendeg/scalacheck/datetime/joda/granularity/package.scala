/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.datetime.joda

import com.fortysevendeg.scalacheck.datetime.Granularity
import org.joda.time.DateTime

package object granularity {

  implicit val seconds: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) => dt.withMillisOfSecond(0)
    val description = "Seconds"
  }

  implicit val minutes: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) => dt.withMillisOfSecond(0).withSecondOfMinute(0)
    val description = "Minutes"
  }

  implicit val hours: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) => dt.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0)
    val description = "Hours"
  }

  implicit val days: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) => dt.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0)
    val description = "Days"
  }

  implicit val years: Granularity[DateTime] = new Granularity[DateTime] {
    val normalize = (dt: DateTime) => dt.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0).withDayOfYear(1)
    val description = "Years"
  }
}

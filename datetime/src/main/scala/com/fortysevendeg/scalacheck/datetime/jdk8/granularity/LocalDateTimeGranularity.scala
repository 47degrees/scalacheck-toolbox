package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.LocalDateTime

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
  * Created by timpigden on 15/02/17.
  * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
  */
object LocalDateTimeGranularity {
  implicit val seconds: Granularity[LocalDateTime] = new Granularity[LocalDateTime] {
    val normalize = (dt: LocalDateTime) => dt.withNano(0)
    val description = "Seconds"
  }

  implicit val minutes: Granularity[LocalDateTime] = new Granularity[LocalDateTime] {
    val normalize = (dt: LocalDateTime) => dt.withNano(0).withSecond(0)
    val description = "Minutes"
  }

  implicit val hours: Granularity[LocalDateTime] = new Granularity[LocalDateTime] {
    val normalize = (dt: LocalDateTime) => dt.withNano(0).withSecond(0).withMinute(0)
    val description = "Hours"
  }

  implicit val days: Granularity[LocalDateTime] = new Granularity[LocalDateTime] {
    val normalize = (dt: LocalDateTime) => dt.withNano(0).withSecond(0).withMinute(0).withHour(0)
    val description = "Days"
  }

  implicit val years: Granularity[LocalDateTime] = new Granularity[LocalDateTime] {
    // Set the day of year before the hour as some days (very very rarely) start at 1am.
    // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
    // So Java 8 sets the hour to 1am.
    // If you then set the day of year to Jan 1, and that day starts at 12am,
    // then the granularity has been set wrong in that case. Insane.
    val normalize = (dt: LocalDateTime) => dt.withDayOfYear(1).withNano(0).withSecond(0).withMinute(0).withHour(0)
    val description = "Years"
  }

}

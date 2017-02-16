package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.LocalDate

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
  * Created by timpigden on 15/02/17.
  * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
  */
object LocalDateGranularity {
  implicit val seconds: Granularity[LocalDate] = new Granularity[LocalDate] {
    val normalize = (dt: LocalDate) => dt
    val description = "Seconds"
  }

  implicit val minutes: Granularity[LocalDate] = new Granularity[LocalDate] {
    val normalize = (dt: LocalDate) => dt
    val description = "Minutes"
  }

  implicit val hours: Granularity[LocalDate] = new Granularity[LocalDate] {
    val normalize = (dt: LocalDate) => dt
    val description = "Hours"
  }

  implicit val days: Granularity[LocalDate] = new Granularity[LocalDate] {
    val normalize = (dt: LocalDate) => dt
    val description = "Days"
  }

  implicit val years: Granularity[LocalDate] = new Granularity[LocalDate] {
    // Set the day of year before the hour as some days (very very rarely) start at 1am.
    // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
    // So Java 8 sets the hour to 1am.
    // If you then set the day of year to Jan 1, and that day starts at 12am,
    // then the granularity has been set wrong in that case. Insane.
    val normalize = (dt: LocalDate) => dt.withDayOfYear(1)
    val description = "Years"
  }

}

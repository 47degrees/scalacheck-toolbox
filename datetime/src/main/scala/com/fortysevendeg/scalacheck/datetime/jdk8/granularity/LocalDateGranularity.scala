package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.LocalDate

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
  * Created by timpigden on 15/02/17.
  * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
  */
object LocalDateGranularity {

  case class LocalDateGranularity(description: String, normalize: (LocalDate) => LocalDate) extends Granularity[LocalDate]

  object LocalDateGranularity {
    // we do this because all LocalDate granularities sub-day level are irrelevant
    def apply(description: String): LocalDateGranularity = new LocalDateGranularity(description, (ld) => ld )
  }

  implicit val seconds: Granularity[LocalDate] = LocalDateGranularity("Seconds")
  implicit val minutes: Granularity[LocalDate] = LocalDateGranularity("Minutes")
  implicit val hours: Granularity[LocalDate] = LocalDateGranularity("Hours")
  implicit val days: Granularity[LocalDate] = LocalDateGranularity("Days")

  // Set the day of year before the hour as some days (very very rarely) start at 1am.
  // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
  // So Java 8 sets the hour to 1am.
  // If you then set the day of year to Jan 1, and that day starts at 12am,
  // then the granularity has been set wrong in that case. Insane.
  implicit val years: Granularity[LocalDate] = new LocalDateGranularity("Years", (dt: LocalDate) => dt.withDayOfYear(1))
}

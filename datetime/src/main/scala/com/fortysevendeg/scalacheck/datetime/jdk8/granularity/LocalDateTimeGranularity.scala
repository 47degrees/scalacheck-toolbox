package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.LocalDateTime

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
  * Created by timpigden on 15/02/17.
  * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
  */
object LocalDateTimeGranularity {
  case class LocalDateTimeGranularity(description: String, normalize: (LocalDateTime) => LocalDateTime)  extends Granularity[LocalDateTime]

  val seconds = LocalDateTimeGranularity("Seconds", _.withNano(0))
  val minutes = LocalDateTimeGranularity("Minutes", _.withNano(0).withSecond(0))
  val hours = LocalDateTimeGranularity("Hours", _.withNano(0).withSecond(0).withMinute(0))
  val days = LocalDateTimeGranularity("Days", _.withNano(0).withSecond(0).withMinute(0).withHour(0))


  // Set the day of year before the hour as some days (very very rarely) start at 1am.
  // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
  // So Java 8 sets the hour to 1am.
  // If you then set the day of year to Jan 1, and that day starts at 12am,
  // then the granularity has been set wrong in that case. Insane.
  val years = LocalDateTimeGranularity("Years", _.withDayOfYear(1).withNano(0).withSecond(0).withMinute(0).withHour(0))

}

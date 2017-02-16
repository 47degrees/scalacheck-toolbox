package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.{Instant, ZoneOffset}

import com.fortysevendeg.scalacheck.datetime.Granularity
import java.time.temporal.ChronoField._


/**
  * Created by timpigden on 15/02/17.
  * Copyright (c) Optrak Distribution Software Ltd, Ware 2016
  * Note that instant does not support minute_of_hour and so on. So we convert to utc zdt. this is a bit naughty
  * but granular instants are really useful
  */
object InstantGranularity {
  
  implicit val seconds: Granularity[Instant] = new Granularity[Instant] {
    val normalize = (dt: Instant) =>
      dt.atOffset(ZoneOffset.UTC).withNano(0).toInstant
    val description = "Seconds"
  }

  implicit val minutes: Granularity[Instant] = new Granularity[Instant] {
    val normalize = (dt: Instant) =>
      dt.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).toInstant
    val description = "Minutes"
  }

  implicit val hours: Granularity[Instant] = new Granularity[Instant] {
    val normalize = (dt: Instant) =>
      dt.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).withMinute(0).toInstant
    val description = "Hours"
  }

  implicit val days: Granularity[Instant] = new Granularity[Instant] {
    val normalize = (dt: Instant) =>
      dt.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).withMinute(0).withHour(0).toInstant
    val description = "Days"
  }

  implicit val years: Granularity[Instant] = new Granularity[Instant] {
    // Set the day of year before the hour as some days (very very rarely) start at 1am.
    // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
    // So Java 8 sets the hour to 1am.
    // If you then set the day of year to Jan 1, and that day starts at 12am,
    // then the granularity has been set wrong in that case. Insane.
    val normalize = (dt: Instant) =>
      dt.atOffset(ZoneOffset.UTC).withDayOfYear(1).withNano(0).withSecond(0).withMinute(0).withHour(0).toInstant
    val description = "Years"
  }

}

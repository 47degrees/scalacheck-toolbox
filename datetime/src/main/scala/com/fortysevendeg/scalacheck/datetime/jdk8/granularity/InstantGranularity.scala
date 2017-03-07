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

  case class InstantGranularity(description: String, normalize: (Instant) => Instant) extends Granularity[Instant]

  implicit val seconds: Granularity[Instant] = InstantGranularity("Seconds",
    _.atOffset(ZoneOffset.UTC).withNano(0).toInstant)
  implicit val minutes: Granularity[Instant] = InstantGranularity("Minutes",
    _.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).toInstant)
  implicit val hours: Granularity[Instant] = InstantGranularity("Hours",
    _.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).withMinute(0).toInstant)
  implicit val days: Granularity[Instant] = InstantGranularity("Days",
    _.atOffset(ZoneOffset.UTC).withNano(0).withSecond(0).withMinute(0).withHour(0).toInstant)

  // Set the day of year before the hour as some days (very very rarely) start at 1am.
  // It is therefore possible to set the hour of day to zero on a day where it starts at 1am.
  // So Java 8 sets the hour to 1am.
  // If you then set the day of year to Jan 1, and that day starts at 12am,
  // then the granularity has been set wrong in that case. Insane.
  implicit val years: Granularity[Instant] = InstantGranularity("Years",
      _.atOffset(ZoneOffset.UTC).withDayOfYear(1).withNano(0).withSecond(0).withMinute(0).withHour(0).toInstant)

}

package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.{LocalDateTime, LocalTime}

import com.fortysevendeg.scalacheck.datetime.Granularity

/**
  * Created by timpigden on 15/02/17.
  */
object LocalTimeGranularity {
  implicit val seconds: Granularity[LocalTime] = new Granularity[LocalTime] {
    val normalize = (dt: LocalTime) => dt.withNano(0)
    val description = "Seconds"
  }

  implicit val minutes: Granularity[LocalTime] = new Granularity[LocalTime] {
    val normalize = (dt: LocalTime) => dt.withNano(0).withSecond(0)
    val description = "Minutes"
  }

  implicit val hours: Granularity[LocalTime] = new Granularity[LocalTime] {
    val normalize = (dt: LocalTime) => dt.withNano(0).withSecond(0).withMinute(0)
    val description = "Hours"
  }
  implicit val days: Granularity[LocalTime] = new Granularity[LocalTime] {
    val normalize = (dt: LocalTime) => dt.withNano(0).withSecond(0).withMinute(0).withHour(0)
    val description = "Days"
  }
  

}

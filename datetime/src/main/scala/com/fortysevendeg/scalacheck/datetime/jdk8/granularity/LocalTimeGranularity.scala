package com.fortysevendeg.scalacheck.datetime.jdk8.granularity

import java.time.LocalTime

import com.fortysevendeg.scalacheck.datetime.Granularity

object LocalTimeGranularity {

  case class LocalTimeGranularity(description: String, normalize: LocalTime => LocalTime)
      extends Granularity[LocalTime]

  val seconds = LocalTimeGranularity("Seconds", _.withNano(0))
  val minutes = LocalTimeGranularity("Minutes", _.withNano(0).withSecond(0))
  val hours   = LocalTimeGranularity("Hours", _.withNano(0).withSecond(0).withMinute(0))
  val days    = LocalTimeGranularity("Days", _.withNano(0).withSecond(0).withMinute(0).withHour(0))

}

package com.fortysevendeg.scalacheck.datetime

trait YearRange {
  val min: Int
  val max: Int
}

object YearRange {

  /** The default range of years. One less than the maximum, to prevent occasional long overflow. */
  implicit def default: YearRange = new YearRange {
    val min: Int = -292278993
    val max: Int = 292278993
  }
}

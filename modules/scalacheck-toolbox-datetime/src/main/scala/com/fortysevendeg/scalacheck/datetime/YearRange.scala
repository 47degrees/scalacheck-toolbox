package com.fortysevendeg.scalacheck.datetime

trait YearRange {
  val min: Int
  val max: Int
}

object YearRange {

  /** The default range of years. Set to a safe subset shared by both `java.time` and `org.joda.time`. */
  implicit val default: YearRange = new YearRange {
    val min: Int = -292275054
    val max: Int = 292278993
  }

  /** Defines a year range between 1970 and the max year you specify. */
  def epochTo(year: Int) = new YearRange {
    val min = 1970
    val max = year
  }

  /** Defines a year range between your own defined min and max years. */
  def between(minYear: Int, maxYear: Int) = new YearRange {
    val min = minYear
    val max = maxYear
  }
}

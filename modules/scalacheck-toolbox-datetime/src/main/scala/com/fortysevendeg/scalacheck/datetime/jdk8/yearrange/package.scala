package com.fortysevendeg.scalacheck.datetime.jdk8

import com.fortysevendeg.scalacheck.datetime.YearRange

package object yearrange {

  /** Defines a year range between 1970 and the max year you specify. */
  def epochTo(year: Int) = new YearRange {
    override val min = 1970
    val max          = year
  }

  /** Defines a year range between your own defined min and max years. */
  def of(minYear: Int, maxYear: Int) = new YearRange {
    override val min = minYear
    val max          = maxYear
  }
}

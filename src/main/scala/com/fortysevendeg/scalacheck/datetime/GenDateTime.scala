package com.fortysevendeg.scalacheck.datetime

import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary

import com.fortysevendeg.scalacheck.datetime.typeclasses._

/**
  * Some generators for working with dates and times.
  */
object GenDateTime {

  /**
    * Generates a <code>DateTime</code> between the given <code>dateTime</code>x and the end of the <code>period</code>
    * @param dateTime A <code>DateTime</code> to calculate the period offsets from.
    * @param period An offset from <code>dateTime</code>, serving as an upper bound for generated <code>DateTime</code>s. Can be negative, denoting an offset <i>before</i> the provided <code>DateTime</code>.
    * @return A <code>DateTime</code> generator for <code>DateTime</code>s within the expected range.
    */
  def genDateTimeWithinPeriod[D, R](dateTime: D, range: R)(implicit scDateTime: ScalaCheckDateTimeInfra[D, R]): Gen[D] = {

    val diffMillis = scDateTime.getMillis(scDateTime.addRange(dateTime, range)) - scDateTime.getMillis(dateTime)

    Gen.choose(0L min diffMillis, 0L max diffMillis).map(millis => scDateTime.addMillis(dateTime, millis))
  }
}

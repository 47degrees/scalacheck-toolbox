package com.fortysevendeg.scalacheck.datetime

import scala.util.Try

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
  def genDateTimeWithinRange[D, R](dateTime: D, range: R)(implicit scDateTime: ScalaCheckDateTimeInfra[D, R], granularity: Granularity[D]): Gen[D] = {
    for {
      addedRange <- Try(Gen.const(scDateTime.addRange(dateTime, range))).getOrElse(Gen.fail)
      diffMillis = scDateTime.getMillis(addedRange) - scDateTime.getMillis(dateTime)
      millis <- Gen.choose(0L min diffMillis, 0L max diffMillis)
    } yield granularity.normalize(scDateTime.addMillis(dateTime, millis))
  }
}

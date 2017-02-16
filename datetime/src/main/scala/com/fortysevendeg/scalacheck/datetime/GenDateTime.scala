/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

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
    * Generates a <code>DateTime</code> between the given <code>dateTime</code>x and the end of the <code>range</code>
    * @param dateTime A <code>DateTime</code> to calculate the period offsets from.
    * @param period An offset from <code>dateTime</code>, serving as an upper bound for generated <code>DateTime</code>s. Can be negative, denoting an offset <i>before</i> the provided <code>DateTime</code>.
    * @return A <code>DateTime</code> generator for <code>DateTime</code>s within the expected range.
    */
  def genDateTimeWithinRange[D, R](dateTime: D, range: R)(implicit scDateTime: ScalaCheckDateTimeInfra[D, R], granularity: Granularity[D]): Gen[D] = {
    for {
      addedRange <- Try(Gen.const(scDateTime.addRange(dateTime, range))).getOrElse(Gen.fail)
      diffMillis = scDateTime.getMillis(addedRange) - scDateTime.getMillis(dateTime)
      millis <- Gen.choose(0L min diffMillis, 0L max diffMillis)
      normalized = granularity.normalize(scDateTime.addMillis(dateTime, millis))
      (early, late) = if(scDateTime.isBefore(dateTime, addedRange)) (dateTime, addedRange) else (addedRange, dateTime)
      inRange = !(scDateTime.isBefore(normalized, early) || scDateTime.isBefore(late, normalized))
      ok <- if (inRange) Gen.const(normalized) else Gen.fail
    } yield ok
  }

  def genDateWithinRange[D, R](date: D, range: R)(implicit scDate: ScalaCheckDateInfra[D, R], granularity: Granularity[D]): Gen[D] = {
    for {
      addedRange <- Try(Gen.const(scDate.addRange(date, range))).getOrElse(Gen.fail)
      diffDays = scDate.getDiffDays(date, addedRange)
      days <- Gen.choose(0 min diffDays, 0 max diffDays)
      normalized = granularity.normalize(scDate.addDays(date, days))
      (early, late) = if(scDate.isBefore(date, addedRange)) (date, addedRange) else (addedRange, date)
      inRange = !(scDate.isBefore(normalized, early) || scDate.isBefore(late, normalized))
      ok <- if (inRange) Gen.const(normalized) else Gen.fail
    } yield ok
  }


  def genTimeBetween[D](from: D, to: D)(implicit scTime: ScalaCheckTimeInfra[D], granularity: Granularity[D]): Gen[D] = {
    val (early, late) = if (scTime.isBefore(from, to)) (from, to) else (to, from)
    val diffMillis= scTime.diffMillis(early, late)
    for {
      millis <- Gen.choose(0L, diffMillis)
      normalized = granularity.normalize(scTime.addMillis(early, millis))
      ok <- if(scTime.isBefore(normalized, early) || scTime.isBefore(late, normalized)) Gen.fail else Gen.const(normalized)
    } yield ok
  }


}

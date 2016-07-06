package com.fortysevendeg.scalacheck.datetime.joda

import org.scalacheck._
import org.scalacheck.Prop._

import org.joda.time._
import org.joda.time.format.PeriodFormat

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.joda._

import GenJoda._

object GenJodaProperties extends Properties("Joda Generators") {

  /*
   * These properties check that the construction of the periods does not fail. Some (like years) have a restricted range of values.
   */

  property("genYearsPeriod creates valid year periods")     = forAll(genYearsPeriod)   { _ => passed }

  property("genMonthsPeriod creates valid month periods")   = forAll(genMonthsPeriod)  { _ => passed }

  property("genWeeksPeriod creates valid week periods")     = forAll(genWeeksPeriod)   { _ => passed }

  property("genDaysPeriod creates valid day periods")       = forAll(genDaysPeriod)    { _ => passed }

  property("genHoursPeriod creates valid hour periods")     = forAll(genHoursPeriod)   { _ => passed }

  property("genMinutesPeriod creates valid minute periods") = forAll(genMinutesPeriod) { _ => passed }

  property("genSecondsPeriod creates valid second periods") = forAll(genSecondsPeriod) { _ => passed }

  property("genPeriod creates valid periods containing a selection of other periods") = forAll(genPeriod) { _ => passed }

  property("genDateTimeWithinRange for Joda should generate DateTimes between the given date and the end of the specified Period") = forAll(genPeriod) { p =>

    val now = new DateTime()

    forAll(genDateTimeWithinRange(now, p)) { generated =>

      // if period is negative, then periodBoundary will be before now
      val periodBoundary = now.plus(p)

      val resultText = s"""Period:          ${PeriodFormat.getDefault().print(p)}
                          |Now:             $now
                          |Generated:       $generated
                          |Period Boundary: $periodBoundary""".stripMargin

      val (lowerBound, upperBound) = if(periodBoundary.isAfter(now)) (now, periodBoundary) else (periodBoundary, now)

      val check = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                  (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

      check :| resultText
    }
  }
}

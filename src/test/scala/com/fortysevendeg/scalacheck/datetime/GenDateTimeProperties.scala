package com.fortysevendeg.scalacheck.datetime

import org.scalacheck._
import org.scalacheck.Prop._

import org.joda.time._
import org.joda.time.format.PeriodFormat

import GenDateTime._

object GenDateTimeProperties extends Properties("Date Time Generators") {

  /*
   *  These properties check that the construction of the periods does not fail. Some (like years) have a restricted range of values.
   */

  property("genYearsPeriod creates valid year periods")     = forAll(genYearsPeriod)   { _ => passed }

  property("genMonthsPeriod creates valid month periods")   = forAll(genMonthsPeriod)  { _ => passed }

  property("genWeeksPeriod creates valid week periods")     = forAll(genWeeksPeriod)   { _ => passed }

  property("genDaysPeriod creates valid day periods")       = forAll(genDaysPeriod)    { _ => passed }

  property("genHoursPeriod creates valid hour periods")     = forAll(genHoursPeriod)   { _ => passed }

  property("genMinutesPeriod creates valid minute periods") = forAll(genMinutesPeriod) { _ => passed }

  property("genSecondsPeriod creates valid second periods") = forAll(genSecondsPeriod) { _ => passed }

  property("genDateTimeWithinPeriod should generate DateTimes between the given date and the end of the specified period") = forAll(genPeriod) { p =>

    val now = new DateTime()

    forAll(genDateTimeWithinPeriod(now, p)) { generated =>

      // if period is negative, then periodBoundary will be before now
      val periodBoundary = now.plus(p)

      val resultText = s"""Period:          ${PeriodFormat.getDefault().print(p)}
                          |Now:             $now
                          |Generated:       $generated
                          |Period Boundary: $periodBoundary""".stripMargin

      val check = if (periodBoundary.isAfter(now)) { // period is positive
        (now.isBefore(generated) || now.isEqual(generated)) && (periodBoundary.isAfter(generated) || periodBoundary.isEqual(generated))
      } else { // period is negative
        (periodBoundary.isBefore(generated) || periodBoundary.isEqual(generated)) && (now.isAfter(generated) || now.isEqual(generated))
      }

      check :| resultText
    }
  }
}

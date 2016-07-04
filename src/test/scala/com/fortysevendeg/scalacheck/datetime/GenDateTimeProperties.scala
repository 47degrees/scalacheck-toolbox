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

  property("genDateTimeBeforeAndAfter should only be generated within the specified period, both before and after the given time") = forAll(genPeriod) { p =>

    val now = new DateTime()

    forAll(genDateTimeBeforeAndAfter(now, p)) { generated =>

      // if period is negative, then "maxBoundary" will be before now, and vice versa
      val maxBoundary = now.plus(p)
      val minBoundary = now.minus(p)

      val resultText = s"""Period:       ${PeriodFormat.getDefault().print(p)}
                          |Now:          $now
                          |Generated:    $generated
                          |Min boundary: $minBoundary
                          |Max Boundary: $maxBoundary""".stripMargin

      val check = if (minBoundary.isBefore(now)) { // period is positive
        (minBoundary.isBefore(generated) || minBoundary.isEqual(generated)) && (maxBoundary.isAfter(generated) || maxBoundary.isEqual(generated))
      } else { // period is negative
        (maxBoundary.isBefore(generated) || maxBoundary.isEqual(generated)) && (minBoundary.isAfter(generated) || minBoundary.isEqual(generated))

      }

      check :| resultText
    }
  }
}

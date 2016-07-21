package com.fortysevendeg.scalacheck.datetime.joda

import org.scalacheck._
import org.scalacheck.Prop._

import org.joda.time._
import org.joda.time.format.PeriodFormat

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.joda._

import GenJoda._

import com.fortysevendeg.scalacheck.datetime.Granularity

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

  property("genDateTime creates valid DateTime instances (with no granularity)") = forAll(genDateTime) { _ => passed }

  val granularitiesAndPredicates: List[(Granularity[DateTime], DateTime => Boolean)] = {

    def zeroMillis(dt: DateTime)  =                    dt.getMillisOfSecond == 0
    def zeroSeconds(dt: DateTime) = zeroMillis(dt)  && dt.getSecondOfMinute == 0
    def zeroMinutes(dt: DateTime) = zeroSeconds(dt) && dt.getMinuteOfHour == 0
    def zeroHours(dt: DateTime)   = zeroMinutes(dt) && dt.getHourOfDay == 0
    def firstDay(dt: DateTime)    = zeroHours(dt)   && dt.getDayOfYear == 1

    List(
      (granularity.seconds, zeroMillis _),
      (granularity.minutes, zeroSeconds _),
      (granularity.hours, zeroMinutes _),
      (granularity.days, zeroHours _),
      (granularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefault: List[(Granularity[DateTime], DateTime => Boolean)] = (Granularity.identity[DateTime], (_: DateTime) => true) :: granularitiesAndPredicates

  property("genDateTime with a granularity generates appropriate DateTimes") = forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
    implicit val generatedGranularity = granularity

    forAll(genDateTime) { dt =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  property("genDateTimeWithinRange for Joda should generate DateTimes between the given date and the end of the specified Period, with the relevant granularity") =
    forAll(genPeriod, Gen.oneOf(granularitiesAndPredicatesWithDefault)) { case (period, (granularity, predicate)) =>

    implicit val generatedGranularity = granularity

    val now = new DateTime()

    forAll(genDateTimeWithinRange(now, period)) { generated =>

      // if period is negative, then periodBoundary will be before now
      val periodBoundary = now.plus(period)

      val resultText = s"""Period:          ${PeriodFormat.getDefault().print(period)}
                          |Now:             $now
                          |Generated:       $generated
                          |Period Boundary: $periodBoundary
                          |Granularity:     ${granularity.description}""".stripMargin

      val (lowerBound, upperBound) = if(periodBoundary.isAfter(now)) (now, periodBoundary) else (periodBoundary, now)

      val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                       (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

      val granularityCheck = predicate(generated)

      val prop = rangeCheck && granularityCheck

      prop :| resultText
    }
  }
}

/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  override def overrideParameters(p: Test.Parameters): Test.Parameters =
    p.withMinSuccessfulTests(100000)

  /*
   * These properties check that the construction of the periods does not fail. Some (like years) have a restricted range of values.
   */

  property("genYearsPeriod creates valid year periods") = forAll(genYearsPeriod)(_ => passed)

  property("genMonthsPeriod creates valid month periods") = forAll(genMonthsPeriod)(_ => passed)

  property("genWeeksPeriod creates valid week periods") = forAll(genWeeksPeriod)(_ => passed)

  property("genDaysPeriod creates valid day periods") = forAll(genDaysPeriod)(_ => passed)

  property("genHoursPeriod creates valid hour periods") = forAll(genHoursPeriod)(_ => passed)

  property("genMinutesPeriod creates valid minute periods") = forAll(genMinutesPeriod) { _ =>
    passed
  }

  property("genSecondsPeriod creates valid second periods") = forAll(genSecondsPeriod) { _ =>
    passed
  }

  property("genPeriod creates valid periods containing a selection of other periods") =
    forAll(genPeriod)(_ => passed)

  property("genDateTime creates valid DateTime instances (with no granularity)") =
    forAll(genDateTime)(_ => passed)

  property("arbitrary generation creates valid DateTime instances (with no granularity)") = {
    import ArbitraryJoda._
    forAll { dt: DateTime => dt.getZone == DateTimeZone.getDefault }
  }

  property("arbitrary generation creates DateTime instances with right time zone") = {
    implicit val zone = DateTimeZone.UTC
    import ArbitraryJoda._
    forAll { dt: DateTime => dt.getZone == zone }
  }

  val granularitiesAndPredicates: List[(Granularity[DateTime], DateTime => Boolean)] = {

    def zeroMillis(dt: DateTime)  = dt.getMillisOfSecond == 0
    def zeroSeconds(dt: DateTime) = zeroMillis(dt) && dt.getSecondOfMinute == 0
    def zeroMinutes(dt: DateTime) = zeroSeconds(dt) && dt.getMinuteOfHour == 0
    def zeroHours(dt: DateTime)   = zeroMinutes(dt) && dt.getHourOfDay == 0
    def firstDay(dt: DateTime)    = zeroHours(dt) && dt.getDayOfYear == 1

    List(
      (granularity.seconds, zeroMillis _),
      (granularity.minutes, zeroSeconds _),
      (granularity.hours, zeroMinutes _),
      (granularity.days, zeroHours _),
      (granularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefault: List[(Granularity[DateTime], DateTime => Boolean)] =
    (Granularity.identity[DateTime], (_: DateTime) => true) :: granularitiesAndPredicates

  property("genDateTime with a granularity generates appropriate DateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) {
      case (granularity, predicate) =>
        implicit val generatedGranularity = granularity

        forAll(genDateTime)(dt => predicate(dt) :| s"${granularity.description}: $dt")
    }

  property(
    "genDateTimeWithinRange for Joda should generate DateTimes between the given date and the end of the specified Period, with the relevant granularity"
  ) = forAll(genPeriod, Gen.oneOf(granularitiesAndPredicatesWithDefault)) {
    case (period, (granularity, predicate)) =>
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

        val (lowerBound, upperBound) =
          if (periodBoundary.isAfter(now)) (now, periodBoundary) else (periodBoundary, now)

        val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
          (upperBound.isAfter(generated) || upperBound.isEqual(generated))

        val granularityCheck = predicate(generated)

        val prop = rangeCheck && granularityCheck

        prop :| resultText
      }
  }
}

/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

package com.fortysevendeg.scalacheck.datetime.jdk8

import scala.util.Try

import org.scalacheck._
import org.scalacheck.Prop._

import java.time._

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._

import GenJdk8._

import com.fortysevendeg.scalacheck.datetime.Granularity

object GenJdk8Properties extends Properties("Java 8 Generators") {

  property("genDuration creates valid durations") = forAll(genDuration) { _ =>
    passed
  }

  property("genZonedDateTime creates valid times (with no granularity)") =
    forAll(genZonedDateTime) { _ =>
      passed
    }

  property("arbitrary generation creates valid times (with no granularity)") = {
    import ArbitraryJdk8._
    forAll { dt: ZonedDateTime =>
      passed
    }
  }

  val granularitiesAndPredicates: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = {

    import java.time.temporal.ChronoField._

    def zeroNanos(dt: ZonedDateTime)   = dt.get(NANO_OF_SECOND) == 0
    def zeroSeconds(dt: ZonedDateTime) = zeroNanos(dt) && dt.get(SECOND_OF_MINUTE) == 0
    def zeroMinutes(dt: ZonedDateTime) = zeroSeconds(dt) && {
      // The previous second should be in the previous hour.
      // There are cases where half an hour has been taken out of a day,
      // such as +58963572-10-01T02:30+11:00[Australia/Lord_Howe]
      // One second before is 01:59:59!
      val prevSecond = dt.plusSeconds(-1)
      val prevHour   = dt.plusHours(-1)

      (prevSecond.get(HOUR_OF_DAY) == prevHour.get(HOUR_OF_DAY))
    }
    def zeroHours(dt: ZonedDateTime) = zeroMinutes(dt) && {
      // Very very rarely, some days start at 1am, rather than 12am
      // In this case, check that the minute before is in the day before.
      dt.get(HOUR_OF_DAY) match {
        case 0 => true
        case 1 =>
          val prevMinute = dt.plusMinutes(-1)
          val prevDay    = dt.plusDays(-1)

          (prevMinute.get(DAY_OF_YEAR) == prevDay.get(DAY_OF_YEAR)) && (prevMinute
            .get(YEAR) == prevDay.get(YEAR))
        case _ => false
      }
    }
    def firstDay(dt: ZonedDateTime) = zeroHours(dt) && dt.get(DAY_OF_YEAR) == 1

    List(
      (granularity.seconds, zeroNanos _),
      (granularity.minutes, zeroSeconds _),
      (granularity.hours, zeroMinutes _),
      (granularity.days, zeroHours _),
      (granularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefault: List[
    (Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = (
    Granularity.identity[ZonedDateTime],
    (_: ZonedDateTime) => true) :: granularitiesAndPredicates

  property("genZonedDateTime with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) {
      case (granularity, predicate) =>
        implicit val generatedGranularity = granularity

        forAll(genZonedDateTime) { dt =>
          predicate(dt) :| s"${granularity.description}: $dt"
        }
    }

  property("arbitrary generation with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) {
      case (granularity, predicate) =>
        import ArbitraryJdk8._

        implicit val generatedGranularity = granularity

        forAll { dt: ZonedDateTime =>
          predicate(dt) :| s"${granularity.description}: $dt"
        }
    }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRanges(dateTime: ZonedDateTime, d: Duration): Boolean =
    Try(dateTime.plus(d).toInstant().toEpochMilli()).isFailure

  property("genDuration can be added to any date") = forAll(genZonedDateTime, genDuration) {
    (dt, dur) =>
      !tooLargeForAddingRanges(dt, dur) ==> {
        val attempted = Try(dt.plus(dur).toInstant().toEpochMilli())
        attempted.isSuccess :| attempted.toString
      }
  }

  property(
    "genDateTimeWithinRange for Java 8 should generate ZonedDateTimes between the given date and the end of the specified Duration") =
    forAll(genZonedDateTime, genDuration, Gen.oneOf(granularitiesAndPredicatesWithDefault)) {
      case (now, d, (granularity, predicate)) =>
        !tooLargeForAddingRanges(now, d) ==> {

          implicit val generatedGranularity = granularity

          forAll(genDateTimeWithinRange(now, d)) { generated =>
            val durationBoundary = now.plus(d)

            val resultText = s"""Duration:        $d
                            |Duration millis: ${d.toMillis}
                            |Now:             $now
                            |Generated:       $generated
                            |Period Boundary: $durationBoundary
                            |Granularity:     ${granularity.description}""".stripMargin

            val (lowerBound, upperBound) =
              if (durationBoundary.isAfter(now)) (now, durationBoundary)
              else (durationBoundary, now)

            val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
              (upperBound.isAfter(generated) || upperBound.isEqual(generated))

            val granularityCheck = predicate(generated)

            val prop = rangeCheck && granularityCheck

            prop :| resultText
          }
        }
    }
}

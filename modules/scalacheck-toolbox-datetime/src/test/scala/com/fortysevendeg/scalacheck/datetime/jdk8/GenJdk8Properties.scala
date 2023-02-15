/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

import java.time._

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.Granularity
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.jdk8.GenJdk8._
import com.fortysevendeg.scalacheck.datetime.YearRange
import org.scalacheck._
import org.scalacheck.Prop._

import scala.util.Try

object GenJdk8Properties extends Properties("Java 8 Generators") {

  // Guards against generating values well outside of expected ranges, as users may run into JDK bugs
  implicit val yearRange: YearRange = YearRange.between(0, 10000)

  private val twoThousandYearsMillis = (86400000 * 2000 * 365.25).round

  override def overrideParameters(p: Test.Parameters): Test.Parameters =
    p.withMinSuccessfulTests(100000)

  property("genDuration creates valid durations") = forAll(genDuration)(_ => passed)

  property("genZonedDateTime creates valid times (with no granularity)") =
    forAll(genZonedDateTime)(_ => passed)

  property("arbitrary generation creates valid ZonedDateTimes (with no granularity)") = {
    import ArbitraryJdk8._
    forAll((_: ZonedDateTime) => passed)
  }

  property("arbitrary generation creates valid LocalDateTimes (with no granularity)") = {
    import ArbitraryJdk8._
    forAll((_: LocalDateTime) => passed)
  }

  property("arbitrary generation creates valid LocalDates (with no granularity)") = {
    import ArbitraryJdk8._
    forAll((_: LocalDate) => passed)
  }

  property("arbitrary generation creates valid Instants (with no granularity)") = {
    import ArbitraryJdk8._
    forAll((_: Instant) => passed)
  }

  val granularitiesAndPredicates: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = {

    import java.time.temporal.ChronoField._

    // Defines handling the weird scenario where normalizing is impossible due to a sudden timezone switch.
    def timezoneSwitch(dt: ZonedDateTime) = {
      (dt.withHour(0).getHour > 0) ||
      (dt.withMinute(0).getMinute > 0) ||
      (dt.withSecond(0).getSecond > 0) ||
      (dt.withNano(0).getNano > 0)
    }

    def zeroNanos(dt: ZonedDateTime) = timezoneSwitch(dt) || dt.get(NANO_OF_SECOND) == 0
    def zeroSeconds(dt: ZonedDateTime) =
      timezoneSwitch(dt) || (zeroNanos(dt) && dt.get(SECOND_OF_MINUTE) == 0)
    def zeroMinutes(dt: ZonedDateTime) =
      timezoneSwitch(dt) || (zeroSeconds(dt) && dt.get(MINUTE_OF_HOUR) == 0)
    def zeroHours(dt: ZonedDateTime) =
      timezoneSwitch(dt) || (zeroMinutes(dt) && {
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
      })
    def firstDay(dt: ZonedDateTime) =
      timezoneSwitch(dt) || (zeroHours(dt) && dt.get(DAY_OF_YEAR) == 1)

    List(
      (granularity.seconds, zeroNanos),
      (granularity.minutes, zeroSeconds),
      (granularity.hours, zeroMinutes),
      (granularity.days, zeroHours),
      (granularity.years, firstDay)
    )
  }

  val granularitiesAndPredicatesWithDefault: List[
    (Granularity[ZonedDateTime], ZonedDateTime => Boolean)
  ] =
    (Granularity.identity[ZonedDateTime], (_: ZonedDateTime) => true) :: granularitiesAndPredicates

  property("genZonedDateTime with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

      forAll(genZonedDateTime)(dt => predicate(dt) :| s"${granularity.description}: $dt")
    }

  property("arbitrary generation with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

      forAll((dt: ZonedDateTime) => predicate(dt) :| s"${granularity.description}: $dt")
    }

  property("arbitrary generation with a granularity generates appropriate LocalDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

      forAll { (dt: LocalDateTime) =>
        predicate(dt.atZone(ZoneOffset.UTC)) :| s"${granularity.description}: $dt"
      }
    }

  property("arbitrary generation with a granularity generates appropriate LocalDates") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

      forAll { (dt: LocalDate) =>
        predicate(dt.atStartOfDay(ZoneOffset.UTC)) :| s"${granularity.description}: $dt"
      }
    }

  property("arbitrary generation with a granularity generates appropriate Instants") =
    forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

      forAll { (instant: Instant) =>
        predicate(instant.atZone(ZoneOffset.UTC)) :| s"${granularity.description}: $instant"
      }
    }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRanges(dateTime: ZonedDateTime, d: Duration): Boolean =
    Try(dateTime.plus(d).toInstant.toEpochMilli).isFailure

  property("genDuration can be added to any date") = forAll(genZonedDateTime, genDuration) {
    (dt, dur) =>
      !tooLargeForAddingRanges(dt, dur) ==> {
        val attempted = Try(dt.plus(dur).toInstant.toEpochMilli)
        attempted.isSuccess :| attempted.toString
      }
  }

  property("genDurationOf gives a duration within the specified range") = forAll(Gen.posNum[Long]) {
    (range: Long) =>
      forAll(genDurationOf(-range, range)) { dur =>
        val millis    = dur.toMillis
        val predicate = millis >= -range && millis <= range
        predicate :| s"Duration of millis: $millis, range: -$range to $range"
      }
  }

  property(
    "genDateTimeWithinRange for Java 8 should generate ZonedDateTimes between the given date and the end of the specified Duration"
  ) = forAll(
    genZonedDateTime,
    genDurationOf(-twoThousandYearsMillis, twoThousandYearsMillis),
    Gen.oneOf(granularitiesAndPredicatesWithDefault)
  ) { case (now, d, (granularity, predicate)) =>
    !tooLargeForAddingRanges(now, d) ==> {

      implicit val generatedGranularity: Granularity[ZonedDateTime] = granularity

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

        val rangeCheck = (lowerBound
          .isBefore(generated) || granularity.normalize(lowerBound).isEqual(generated)) &&
          (upperBound.isAfter(generated) || upperBound.isEqual(generated))

        val granularityCheck = predicate(generated)

        val prop = rangeCheck && granularityCheck

        prop :| resultText
      }
    }
  }
}

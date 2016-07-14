package com.fortysevendeg.scalacheck.datetime.j8

import scala.util.Try

import org.scalacheck._
import org.scalacheck.Prop._

import java.time._

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.j8._

import GenJ8._

import com.fortysevendeg.scalacheck.datetime.Granularity

object GenJ8Properties extends Properties("Java 8 Generators") {

  property("genDuration creates valid durations") = forAll(genDuration) { _ => passed }

  property("genZonedDateTime created valid times (with no granularity)") = forAll(genZonedDateTime) { _ => passed }

  val granularitiesAndPredicates: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = {

    import java.time.temporal.ChronoField._

    def zeroNanos(dt: ZonedDateTime)   =                    dt.get(NANO_OF_SECOND) == 0
    def zeroSeconds(dt: ZonedDateTime) = zeroNanos(dt)   && dt.get(SECOND_OF_MINUTE) == 0
    def zeroMinutes(dt: ZonedDateTime) = zeroSeconds(dt) && dt.get(MINUTE_OF_HOUR) == 0
    def zeroHours(dt: ZonedDateTime)   = zeroMinutes(dt) && dt.get(HOUR_OF_DAY) == 0
    def firstDay(dt: ZonedDateTime)    = zeroHours(dt)   && dt.get(DAY_OF_YEAR) == 1

    List(
      (granularity.seconds, zeroNanos _),
      (granularity.minutes, zeroSeconds _),
      (granularity.hours, zeroMinutes _),
      (granularity.days, zeroHours _),
      (granularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefault: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = (Granularity.identity[ZonedDateTime], (_: ZonedDateTime) => true) :: granularitiesAndPredicates

  property("genZonedDateTime with a granularity generated appropriate ZonedDateTimes") = forAll(Gen.oneOf(granularitiesAndPredicates)) { case (granularity, predicate) =>

    implicit val generatedGranularity = granularity

    forAll(genZonedDateTime) { dt =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRanges(dateTime: ZonedDateTime, d: Duration): Boolean = {
    Try(dateTime.plus(d).toInstant().toEpochMilli()).isFailure
  }

  property("genDuration can be added to any date") = forAll(genZonedDateTime, genDuration) { (dt, dur) =>
    !tooLargeForAddingRanges(dt, dur) ==> {
      val attempted = Try(dt.plus(dur).toInstant().toEpochMilli())
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genDateTimeWithinRange for Java 8 should generate ZonedDateTimes between the given date and the end of the specified Duration") = forAll(genZonedDateTime, genDuration, Gen.oneOf(granularitiesAndPredicatesWithDefault)) { case (now, d, (granularity, predicate)) =>
    !tooLargeForAddingRanges(now, d) ==> {

      implicit val generatedGranularity = granularity

      forAll(genDateTimeWithinRange(now, d)) { generated =>
        val durationBoundary = now.plus(d)

        val resultText = s"""Duration:        $d
                            |Now:             $now
                            |Generated:       $generated
                            |Period Boundary: $durationBoundary
                            |Granularity      ${granularity.description}""".stripMargin

        val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

        val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                    (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

        val granularityCheck = predicate(generated)

        val prop = rangeCheck && granularityCheck

        prop :| resultText
      }
    }
  }
}

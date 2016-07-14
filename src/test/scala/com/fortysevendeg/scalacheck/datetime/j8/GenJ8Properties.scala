package com.fortysevendeg.scalacheck.datetime.j8

import scala.util.Try

import org.scalacheck._
import org.scalacheck.Prop._

import java.time._

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.j8._

import GenJ8._

object GenJ8Properties extends Properties("Java 8 Generators") {

  property("genDuration creates valid durations") = forAll(genDuration) { _ => passed }

  property("genZonedDateTime created valid times") = forAll(genZonedDateTime) { _ => passed }

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

  property("genDateTimeWithinRange for Java 8 should generate ZonedDateTimes between the given date and the end of the specified Duration") = forAll(genZonedDateTime, genDuration) { (now, d) =>
    !tooLargeForAddingRanges(now, d) ==> {
      forAll(genDateTimeWithinRange(now, d)) { generated =>
        val durationBoundary = now.plus(d)

        val resultText = s"""Duration:        $d
                            |Now:             $now
                            |Generated:       $generated
                            |Period Boundary: $durationBoundary""".stripMargin

        val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

        val check = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                    (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

        check :| resultText
      }
    }
  }
}

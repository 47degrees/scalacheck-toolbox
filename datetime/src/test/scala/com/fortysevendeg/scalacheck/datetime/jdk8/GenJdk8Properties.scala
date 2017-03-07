/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.datetime.jdk8

import scala.util.Try
import org.scalacheck._
import org.scalacheck.Prop._
import java.time._
import java.time.temporal.ChronoField.HOUR_OF_DAY
import java.time.temporal.{Temporal, TemporalAccessor, TemporalUnit}

import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import GenJdk8._
import com.fortysevendeg.scalacheck.datetime.Granularity
import com.fortysevendeg.scalacheck.datetime.jdk8.GenJdk8Properties.GranularitiesAndPredicates
import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.{InstantGranularity, LocalDateGranularity, LocalDateTimeGranularity, LocalTimeGranularity, ZonedDateTimeGranularity}
import java.time.temporal.ChronoUnit._
import java.time.temporal.ChronoField._

object GenJdk8Properties extends Properties("Java 8 Generators") {


  property("genDuration creates valid durations") = forAll(genDuration) { _ => passed }

  property("genInstant creates valid instants") = forAll(genInstant) {_  => passed }

  property("genZonedDateTime creates valid times (with no granularity)") = forAll(genZonedDateTime) { _ => passed }

  property("arbitrary generation creates valid times (with no granularity)") = {
    import ArbitraryJdk8._
    forAll { dt: ZonedDateTime => passed }
  }


  trait GranularitiesAndPredicates[T <: TemporalAccessor] {
    def zeroNanos(dt: T) = dt.get(NANO_OF_SECOND) == 0
    def zeroSeconds(dt: T) = zeroNanos(dt) && dt.get(SECOND_OF_MINUTE) == 0
    def zeroMinutes(dt: T): Boolean
    def zeroHours(dt: T): Boolean
  }

  class ZdtGranularitiesAndPredicates extends GranularitiesAndPredicates[ZonedDateTime] {
    override def zeroMinutes(dt: ZonedDateTime): Boolean = zeroSeconds(dt) && {
      // The previous second should be in the previous hour.
      // There are cases where half an hour has been taken out of a day,
      // such as +58963572-10-01T02:30+11:00[Australia/Lord_Howe]
      // One second before is 01:59:59!
      val prevSecond = dt.plusSeconds(-1)
      val prevHour = dt.plusHours(-1)

      (prevSecond.get(HOUR_OF_DAY) == prevHour.get(HOUR_OF_DAY))
    }

    override def zeroHours(dt: ZonedDateTime)   = zeroMinutes(dt) && {
      // Very very rarely, some days start at 1am, rather than 12am
      // In this case, check that the minute before is in the day before.
      dt.get(HOUR_OF_DAY) match {
        case 0 => true
        case 1 =>
          val prevMinute = dt.plusMinutes(-1)
          val prevDay = dt.plusDays(-1)

          (prevMinute.get(DAY_OF_YEAR) == prevDay.get(DAY_OF_YEAR)) && (prevMinute.get(YEAR) == prevDay.get(YEAR))
        case _ => false
      }
    }
  }


  val granularitiesAndPredicatesZdt: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] = {

    val gp = new ZdtGranularitiesAndPredicates
    import gp._

    def firstDay(dt: ZonedDateTime)    = zeroHours(dt)   && dt.get(DAY_OF_YEAR) == 1

    List(
      (ZonedDateTimeGranularity.seconds, zeroNanos _),
      (ZonedDateTimeGranularity.minutes, zeroSeconds _),
      (ZonedDateTimeGranularity.hours, zeroMinutes _),
      (ZonedDateTimeGranularity.days, zeroHours _),
      (ZonedDateTimeGranularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefaultZdt: List[(Granularity[ZonedDateTime], ZonedDateTime => Boolean)] =
    (Granularity.identity[ZonedDateTime], (_: ZonedDateTime) => true) :: granularitiesAndPredicatesZdt

  property("genZonedDateTime with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesZdt)) { case (granularity, predicate) =>

    implicit val generatedGranularity = granularity

    forAll(genZonedDateTime) { dt =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  property("arbitrary generation with a granularity generates appropriate ZonedDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesZdt)) { case (granularity, predicate) =>
    import ArbitraryJdk8._

    implicit val generatedGranularity = granularity

    forAll { dt: ZonedDateTime =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRangesZdt(dateTime: ZonedDateTime, d: Duration): Boolean = {
    Try(dateTime.plus(d).toInstant().toEpochMilli()).isFailure
  }

  property("genDuration can be added to any date") = forAll(genZonedDateTime, genDuration) { (dt, dur) =>
    !tooLargeForAddingRangesZdt(dt, dur) ==> {
      val attempted = Try(dt.plus(dur).toInstant().toEpochMilli())
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genDateTimeWithinRange for Java 8 should generate ZonedDateTimes between the given date and the end of the specified Duration") =
    forAll(genZonedDateTime, genDuration, Gen.oneOf(granularitiesAndPredicatesWithDefaultZdt)) { case (now, d, (granularity, predicate)) =>
    !tooLargeForAddingRangesZdt(now, d) ==> {

      implicit val generatedGranularity = granularity

      forAll(genDateTimeWithinRange(now, d)) { generated =>
        val durationBoundary = now.plus(d)

        val resultText = s"""Duration:        $d
                            |Duration millis: ${d.toMillis}
                            |Now:             $now
                            |Generated:       $generated
                            |Period Boundary: $durationBoundary
                            |Granularity:     ${granularity.description}""".stripMargin

        val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

        val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                    (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

        val granularityCheck = predicate(generated)

        val prop = rangeCheck && granularityCheck

        prop :| resultText
      }
    }
  }

  class LdtGranularitiesAndPredicates extends GranularitiesAndPredicates[LocalDateTime] {
    override def zeroMinutes(dt: LocalDateTime): Boolean = zeroSeconds(dt) && {
      // The previous second should be in the previous hour.
      // There are cases where half an hour has been taken out of a day,
      // such as +58963572-10-01T02:30+11:00[Australia/Lord_Howe]
      // One second before is 01:59:59!
      val prevSecond = dt.plusSeconds(-1)
      val prevHour = dt.plusHours(-1)

      (prevSecond.get(HOUR_OF_DAY) == prevHour.get(HOUR_OF_DAY))
    }

    override def zeroHours(dt: LocalDateTime)   = zeroMinutes(dt) && {
      // Very very rarely, some days start at 1am, rather than 12am
      // In this case, check that the minute before is in the day before.
      dt.get(HOUR_OF_DAY) match {
        case 0 => true
        case 1 =>
          val prevMinute = dt.plusMinutes(-1)
          val prevDay = dt.plusDays(-1)

          (prevMinute.get(DAY_OF_YEAR) == prevDay.get(DAY_OF_YEAR)) && (prevMinute.get(YEAR) == prevDay.get(YEAR))
        case _ => false
      }
    }
  }

  val granularitiesAndPredicatesLdt: List[(Granularity[LocalDateTime], LocalDateTime => Boolean)] = {

    val gp = new LdtGranularitiesAndPredicates
    import gp._

    def firstDay(dt: LocalDateTime)    = zeroHours(dt)   && dt.get(DAY_OF_YEAR) == 1

    List(
      (LocalDateTimeGranularity.seconds, zeroNanos _),
      (LocalDateTimeGranularity.minutes, zeroSeconds _),
      (LocalDateTimeGranularity.hours, zeroMinutes _),
      (LocalDateTimeGranularity.days, zeroHours _),
      (LocalDateTimeGranularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefaultLdt: List[(Granularity[LocalDateTime], LocalDateTime => Boolean)] =
    (Granularity.identity[LocalDateTime], (_: LocalDateTime) => true) :: granularitiesAndPredicatesLdt

  property("genLocalDateTime with a granularity generates appropriate LocalDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLdt)) { case (granularity, predicate) =>

    implicit val generatedGranularity = granularity

    forAll(genLocalDateTime) { dt =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  property("arbitrary generation with a granularity generates appropriate LocalDateTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLdt)) { case (granularity, predicate) =>
    import ArbitraryJdk8._

    implicit val generatedGranularity = granularity

    forAll { dt: LocalDateTime =>
      predicate(dt) :| s"${granularity.description}: $dt"
    }
  }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRangesLdt(dt: LocalDateTime, dur: Duration): Boolean = {
    Try(dt.plus(dur).toInstant(ZoneOffset.UTC).toEpochMilli()).isFailure
  }

  property("genDuration can be added to any LocalDateTime") = forAll(genLocalDateTime, genDuration) { (dt, dur) =>
    !tooLargeForAddingRangesLdt(dt, dur) ==> {
      val attempted = Try(dt.plus(dur).toInstant(ZoneOffset.UTC).toEpochMilli)
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genDateTimeWithinRange for Java 8 should generate LocalDateTimes between the given date and the end of the specified Duration") =
    forAll(genLocalDateTime, genDuration, Gen.oneOf(granularitiesAndPredicatesWithDefaultLdt)) { case (now, d, (granularity, predicate)) =>
    !tooLargeForAddingRangesLdt(now, d) ==> {

      implicit val generatedGranularity = granularity

      forAll(genDateTimeWithinRange(now, d)) { generated =>
        val durationBoundary = now.plus(d)

        val resultText = s"""Duration:        $d
                            |Duration millis: ${d.toMillis}
                            |Now:             $now
                            |Generated:       $generated
                            |Period Boundary: $durationBoundary
                            |Granularity:     ${granularity.description}""".stripMargin

        val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

        val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
                    (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

        val granularityCheck = predicate(generated)

        val prop = rangeCheck && granularityCheck

        prop :| resultText
      }
    }
  }

  class LdGranularitiesAndPredicates extends GranularitiesAndPredicates[LocalDate] {
    override def zeroMinutes(dt: LocalDate): Boolean = true

    override def zeroHours(dt: LocalDate) = true
  }

  val granularitiesAndPredicatesLd: List[(Granularity[LocalDate], LocalDate => Boolean)] = {

    val gp = new LdGranularitiesAndPredicates
    import gp._

    def firstDay(dt: LocalDate) = dt.get(DAY_OF_YEAR) == 1

    List(
      (LocalDateGranularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefaultLd: List[(Granularity[LocalDate], LocalDate => Boolean)] =
    (Granularity.identity[LocalDate], (_: LocalDate) => true) :: granularitiesAndPredicatesLd

  property("genLocalDate with a granularity generates appropriate LocalDates") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLd)) { case (granularity, predicate) =>

      implicit val generatedGranularity = granularity

      forAll(genLocalDate) { dt =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }

  property("arbitrary generation with a granularity generates appropriate LocalDates") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLd)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity = granularity

      forAll { dt: LocalDate =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRangesLd(dt: LocalDate, period: Period): Boolean = {
    Try {
      val x = dt.plus(period).atTime(LocalTime.MIN).atZone(ZoneOffset.UTC).toInstant.toEpochMilli()
      val y = dt.plus(period).atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant.toEpochMilli()
    }.isFailure
  }

  property("genPeriod can be added to any LocalDate") = forAll(genLocalDate, genPeriod) { (dt, period) =>
    !tooLargeForAddingRangesLd(dt, period) ==> {
      val attempted = Try(dt.plus(period).atStartOfDay(ZoneOffset.UTC).toInstant.toEpochMilli)
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genLocalDateWithinRange for Java 8 should generate LocalDates between the given date and the end of the specified Duration") =
    forAll(genLocalDate, genPeriod, Gen.oneOf(granularitiesAndPredicatesWithDefaultLd)) { case (now, period, (granularity, predicate)) =>
      !tooLargeForAddingRangesLd(now, period) ==> {

        implicit val generatedGranularity = granularity

        forAll(genDateWithinRange(now, period)) { generated =>
          val durationBoundary = now.plusDays(period.getDays)

          val resultText = s"""Period:        $period
                              |Now:             $now
                              |Generated:       $generated
                              |Period Boundary: $durationBoundary
                              |Granularity:     ${granularity.description}""".stripMargin

          val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

          val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.isEqual(generated)) &&
            (upperBound.isAfter(generated)  || upperBound.isEqual(generated))

          val granularityCheck = predicate(generated)

          val prop = rangeCheck && granularityCheck

          prop :| resultText
        }
      }
    }

  class LtGranularitiesAndPredicates extends GranularitiesAndPredicates[LocalTime] {
    override def zeroMinutes(dt: LocalTime): Boolean = zeroSeconds(dt) && {
      // The previous second should be in the previous hour.
      // There are cases where half an hour has been taken out of a day,
      // such as +58963572-10-01T02:30+11:00[Australia/Lord_Howe]
      // One second before is 01:59:59!
      val prevSecond = dt.plusSeconds(-1)
      val prevHour = dt.plusHours(-1)

      (prevSecond.get(HOUR_OF_DAY) == prevHour.get(HOUR_OF_DAY))
    }

    override def zeroHours(dt: LocalTime)   = zeroMinutes(dt) && dt.get(HOUR_OF_DAY) == 0
  }

  val granularitiesAndPredicatesLt: List[(Granularity[LocalTime], LocalTime => Boolean)] = {

    val gp = new LtGranularitiesAndPredicates
    import gp._

    List(
      (LocalTimeGranularity.seconds, zeroNanos _),
      (LocalTimeGranularity.minutes, zeroSeconds _),
      (LocalTimeGranularity.hours, zeroMinutes _),
      (LocalTimeGranularity.days, zeroHours _)
    )
  }

  val granularitiesAndPredicatesWithDefaultLt: List[(Granularity[LocalTime], LocalTime => Boolean)] =
    (Granularity.identity[LocalTime], (_: LocalTime) => true) :: granularitiesAndPredicatesLt

  property("genLocalTime with a granularity generates appropriate LocalTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLt)) { case (granularity, predicate) =>

      implicit val generatedGranularity = granularity

      forAll(genLocalTime) { dt =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }

  property("arbitrary generation with a granularity generates appropriate LocalTimes") =
    forAll(Gen.oneOf(granularitiesAndPredicatesLt)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity = granularity

      forAll { dt: LocalTime =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRangesLt(dt: LocalTime, dur: Duration): Boolean = {
    Try(dt.plusNanos(dur.getNano)).isFailure
  }

  property("genDuration can be added to any LocalTime") = forAll(genLocalTime, genDuration) { (dt, dur) =>
    !tooLargeForAddingRangesLt(dt, dur) ==> {
      val attempted = Try(dt.plusNanos(dur.getNano))
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genDateTimeWithinRange for Java 8 should generate LocalTimes between the given date and the end of the specified Duration") =
    forAll(genLocalTime, genLocalTime, Gen.oneOf(granularitiesAndPredicatesWithDefaultLt)) { case (earliest, latest, (granularity, predicate)) =>

      implicit val generatedGranularity = granularity

      forAll(genTimeBetween(earliest, latest)) { generated =>

        val resultText =
          s"""Earliest:     $earliest
             |Latest:       $latest
             |Generated:    $generated
             |Granularity:  ${granularity.description}""".stripMargin

          val (lowerBound, upperBound) = if(earliest.isAfter(latest)) (latest, earliest) else (earliest, latest)
          val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.equals(generated)) &&
            (upperBound.isAfter(generated)  || upperBound.
              equals(generated))
          val granularityCheck = predicate(generated)
          val prop = rangeCheck && granularityCheck

          prop :| resultText
        }
      }


  class InstantGranularitiesAndPredicates extends GranularitiesAndPredicates[Instant] {
    override def zeroNanos(dt: Instant) = dt.atOffset(ZoneOffset.UTC).get(NANO_OF_SECOND) == 0
    override def zeroSeconds(dt: Instant) = zeroNanos(dt) && dt.atOffset(ZoneOffset.UTC).get(SECOND_OF_MINUTE) == 0

    override def zeroMinutes(dt: Instant): Boolean = {
      val res = zeroSeconds(dt) && dt.atOffset(ZoneOffset.UTC).get(MINUTE_OF_HOUR) == 0
      res
    }

    override def zeroHours(dt: Instant)   = zeroMinutes(dt) && {
      // Very very rarely, some days start at 1am, rather than 12am
      // In this case, check that the minute before is in the day before.
      val zdt = dt.atOffset(ZoneOffset.UTC)
      zdt.get(HOUR_OF_DAY) match {
        case 0 => true
        case 1 =>
          val prevMinute = zdt.minus(Duration.ofMinutes(1))
          val prevDay = zdt.minus(Duration.ofDays(1))

          (prevMinute.get(DAY_OF_YEAR) == prevDay.get(DAY_OF_YEAR)) && (prevMinute.get(YEAR) == prevDay.get(YEAR))
        case _ => false
      }
    }
  }

  val granularitiesAndPredicatesInstant: List[(Granularity[Instant], Instant => Boolean)] = {

    val gp = new InstantGranularitiesAndPredicates
    import gp._

    def firstDay(dt: Instant) = zeroHours(dt)   && dt.atOffset(ZoneOffset.UTC).get(DAY_OF_YEAR) == 1

    List(
      (InstantGranularity.seconds, zeroNanos _),
      (InstantGranularity.minutes, zeroSeconds _),
      (InstantGranularity.hours, zeroMinutes _),
      (InstantGranularity.days, zeroHours _),
      (InstantGranularity.years, firstDay _)
    )
  }

  val granularitiesAndPredicatesWithDefaultInstant: List[(Granularity[Instant], Instant => Boolean)] =
    (Granularity.identity[Instant], (_: Instant) => true) :: granularitiesAndPredicatesInstant

  property("genInstant with a granularity generates appropriate Instants") =
    forAll(Gen.oneOf(granularitiesAndPredicatesInstant)) { case (granularity, predicate) =>

      implicit val generatedGranularity = granularity

      forAll(genInstant) { dt =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }


  property("arbitrary generation with a granularity generates appropriate Instants") =
    forAll(Gen.oneOf(granularitiesAndPredicatesInstant)) { case (granularity, predicate) =>
      import ArbitraryJdk8._

      implicit val generatedGranularity = granularity

      forAll { dt: Instant =>
        predicate(dt) :| s"${granularity.description}: $dt"
      }
    }

  // Guards against adding a duration to a datetime which cannot represent millis in a long, causing an exception.
  private[this] def tooLargeForAddingRangesInstant(dateTime: Instant, d: Duration): Boolean = {
    Try(dateTime.plus(d).toEpochMilli()).isFailure
  }

  property("genDuration can be added to any date") = forAll(genInstant, genDuration) { (dt, dur) =>
    !tooLargeForAddingRangesInstant(dt, dur) ==> {
      val attempted = Try(dt.plus(dur).toEpochMilli())
      attempted.isSuccess :|  attempted.toString
    }
  }

  property("genDateTimeWithinRange for Java 8 should generate Instants between the given date and the end of the specified Duration") =
    forAll(genInstant, genDuration, Gen.oneOf(granularitiesAndPredicatesWithDefaultInstant)) { case (now, d, (granularity, predicate)) =>
      !tooLargeForAddingRangesInstant(now, d) ==> {

        implicit val generatedGranularity = granularity

        forAll(genDateTimeWithinRange(now, d)) { generated =>
          val durationBoundary = now.plus(d)

          val resultText = s"""Duration:        $d
                              |Duration millis: ${d.toMillis}
                              |Now:             $now
                              |Generated:       $generated
                              |Period Boundary: $durationBoundary
                              |Granularity:     ${granularity.description}""".stripMargin

          val (lowerBound, upperBound) = if(durationBoundary.isAfter(now)) (now, durationBoundary) else (durationBoundary, now)

          val rangeCheck = (lowerBound.isBefore(generated) || lowerBound.equals(generated)) &&
            (upperBound.isAfter(generated)  || upperBound.equals(generated))

          val granularityCheck = predicate(generated)

          val prop = rangeCheck && granularityCheck

          prop :| resultText
        }
      }
    }
}

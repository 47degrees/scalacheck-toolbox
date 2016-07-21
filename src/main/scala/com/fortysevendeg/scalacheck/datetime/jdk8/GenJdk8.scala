package com.fortysevendeg.scalacheck.datetime.jdk8

import collection.JavaConverters._

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

import java.time._
import java.time.temporal.ChronoUnit.MILLIS

import com.fortysevendeg.scalacheck.datetime.Granularity

trait GenJdk8 {

  def genZonedDateTime(implicit granularity: Granularity[ZonedDateTime]): Gen[ZonedDateTime] = for {
    year <- Gen.choose(-292278994, 292278994)
    month <- Gen.choose(1, 12)
    maxDaysInMonth = Month.of(month).length(Year.of(year).isLeap)
    dayOfMonth <- Gen.choose(1, maxDaysInMonth)
    hour <- Gen.choose(0, 23)
    minute <- Gen.choose(0, 59)
    second <- Gen.choose(0, 59)
    nanoOfSecond <- Gen.choose(0, 999999999)
    zoneId <- Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toList)
  } yield granularity.normalize(ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, ZoneId.of(zoneId)))

  val genDuration: Gen[Duration] = Gen.choose(Long.MinValue, Long.MaxValue / 1000).map(l => Duration.of(l, MILLIS))
}

object GenJdk8 extends GenJdk8

object ArbitraryJdk8 extends GenJdk8 {
  implicit val arbJdk8: Arbitrary[ZonedDateTime] = Arbitrary(genZonedDateTime)
}

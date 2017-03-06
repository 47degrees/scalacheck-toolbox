/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.datetime.instances

import com.fortysevendeg.scalacheck.datetime.typeclasses._
import java.time._
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit.MILLIS

trait Jdk8Instances {
   
  implicit val jdkForDurationZonedDateTime: ScalaCheckDateTimeInfra[ZonedDateTime, Duration] = new ScalaCheckDateTimeInfra[ZonedDateTime, Duration] {
    def addRange(zonedDateTime: ZonedDateTime, duration: Duration): ZonedDateTime = zonedDateTime.plus(duration)
    def addMillis(zonedDateTime: ZonedDateTime, millis: Long): ZonedDateTime = zonedDateTime.plus(millis, MILLIS)
    def getMillis(zonedDateTime: ZonedDateTime): Long = zonedDateTime.toInstant.toEpochMilli
    override def isBefore(dt1: ZonedDateTime, dt2: ZonedDateTime): Boolean = dt1.isBefore(dt2)
  }

  implicit val jdkForDurationLocalDateTime: ScalaCheckDateTimeInfra[LocalDateTime, Duration] = new ScalaCheckDateTimeInfra[LocalDateTime, Duration] {
    def addRange(localDateTime: LocalDateTime, duration: Duration): LocalDateTime = localDateTime.plus(duration)
    def addMillis(localDateTime: LocalDateTime, millis: Long): LocalDateTime = localDateTime.plus(millis, MILLIS)
    def getMillis(localDateTime: LocalDateTime): Long = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli
    override def isBefore(dt1: LocalDateTime, dt2: LocalDateTime): Boolean = dt1.isBefore(dt2)
  }

  implicit val jdkForDurationLocalDate: ScalaCheckDateInfra[LocalDate, Period] = new ScalaCheckDateInfra[LocalDate, Period] {
    override def addRange(date: LocalDate, range: Period): LocalDate = date.plus(range)
    override def getDiffDays(from: LocalDate, to: LocalDate): Int = Period.between(from, to).getDays
    override def addDays(dateTime: LocalDate, days: Int): LocalDate = dateTime.plusDays(days)
    override def isBefore(d1: LocalDate, d2: LocalDate): Boolean = d1.isBefore(d2)
  }

  implicit val jdkForDurationTime: ScalaCheckTimeInfra[LocalTime] = new ScalaCheckTimeInfra[LocalTime] {
    override def isBefore(t1: LocalTime, t2: LocalTime): Boolean = t1.isBefore(t2)
    override def diffMillis(lower: LocalTime, higher: LocalTime): Long = Duration.between(lower, higher).toMillis
    override def addMillis(d: LocalTime, millis: Long): LocalTime = d.plus(Duration.ofMillis(millis))
  }

  implicit val jdkForDurationInstant: ScalaCheckDateTimeInfra[Instant, Duration] = new ScalaCheckDateTimeInfra[Instant, Duration] {
    def addRange(Instant: Instant, duration: Duration): Instant = Instant.plus(duration)
    def addMillis(Instant: Instant, millis: Long): Instant = Instant.plus(millis, MILLIS)
    def getMillis(Instant: Instant): Long = Instant.toEpochMilli
    override def isBefore(dt1: Instant, dt2: Instant): Boolean = dt1.isBefore(dt2)
  }
}

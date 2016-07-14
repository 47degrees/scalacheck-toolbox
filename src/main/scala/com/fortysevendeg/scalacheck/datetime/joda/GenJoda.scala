package com.fortysevendeg.scalacheck.datetime.joda

import com.fortysevendeg.scalacheck.datetime.Granularity

import org.scalacheck.Gen
import org.joda.time._

import org.scalacheck.Arbitrary.arbitrary

/**
  * Generators specific for Joda time.
  */
object GenJoda {

  /** A <code>Years</code> period generator. */
  val genYearsPeriod: Gen[Years] = Gen.choose(-292275054, 292278993).map(Years.ZERO.plus(_)) // Years.MIN_VALUE produces exception-throwing results

  /** A <code>Months</code> period generator. */
  val genMonthsPeriod: Gen[Months] = Gen.choose(Months.MIN_VALUE.getMonths, Months.MAX_VALUE.getMonths).map(Months.ZERO.plus(_))

  /** A <code>Weeks</code> period generator. */
  val genWeeksPeriod: Gen[Weeks] = Gen.choose(Weeks.MIN_VALUE.getWeeks, Weeks.MAX_VALUE.getWeeks).map(Weeks.ZERO.plus(_))

  /** A <code>Days</code> period generator. */
  val genDaysPeriod: Gen[Days] = Gen.choose(Days.MIN_VALUE.getDays, Days.MAX_VALUE.getDays).map(Days.ZERO.plus(_))

  /** A <code>Hours</code> period generator. */
  val genHoursPeriod: Gen[Hours] = Gen.choose(Hours.MIN_VALUE.getHours, Hours.MAX_VALUE.getHours).map(Hours.ZERO.plus(_))

  /** A <code>Minutes</code> period generator. */
  val genMinutesPeriod: Gen[Minutes] = Gen.choose(Minutes.MIN_VALUE.getMinutes, Minutes.MAX_VALUE.getMinutes).map(Minutes.ZERO.plus(_))

  /** A <code>Seconds</code> period generator. */
  val genSecondsPeriod: Gen[Seconds] = Gen.choose(Seconds.MIN_VALUE.getSeconds, Seconds.MAX_VALUE.getSeconds).map(Seconds.ZERO.plus(_))

  /** A <code>Period</code> generator consisting of years, days, hours, minutes, seconds and millis. */
  val genPeriod: Gen[Period] = for {
    years <- genYearsPeriod
    days <- Gen.choose(1, 365)
    hours <- Gen.choose(0, 23)
    minutes <- Gen.choose(0, 59)
    seconds <- Gen.choose(0, 59)
    millis <- Gen.choose(0, 999)
  } yield Period.years(years.getYears).withDays(days).withHours(hours).withMinutes(minutes).withSeconds(seconds).withMillis(millis)

  /** A <code>DateTime</code> generator. */
  def genDateTime(implicit granularity: Granularity[DateTime]): Gen[DateTime] = for {
    year <- Gen.choose(-292275055,292278994)
    month <- Gen.choose(1, 12)
    yearAndMonthDt = new DateTime(year, month, 1, 0, 0)
    dayOfMonth <- Gen.choose(1, yearAndMonthDt.dayOfMonth.getMaximumValue)
    hourOfDay <- Gen.choose(0, 23)
    minuteOfHour <- Gen.choose(0, 59)
    secondOfMinute <- Gen.choose(0, 59)
    millisOfSecond <- Gen.choose(0, 999)
  } yield granularity.normalize(new DateTime(year, month, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond))
}

package com.fortysevendeg.scalacheck.datetime

import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary

import org.joda.time._

/**
  * Some generators for working with dates and times.
  */
object GenDateTime {


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

  /**
    * A <code>Period</code> generator consisting of years, days, hours, minutes, seconds and millis.
    */
  val genPeriod: Gen[Period] = for {
    years <- genYearsPeriod
    days <- Gen.choose(1, 365)
    hours <- Gen.choose(0, 23)
    minutes <- Gen.choose(0, 59)
    seconds <- Gen.choose(0, 59)
    millis <- Gen.choose(0, 999)
  } yield Period.years(years.getYears).withDays(days).withHours(hours).withMinutes(minutes).withSeconds(seconds).withMillis(millis)

  /**
    * Generates a <code>DateTime</code> between the given <code>dateTime</code>x and the end of the <code>period</code>
    * @param dateTime A <code>DateTime</code> to calculate the period offsets from.
    * @param period An offset from <code>dateTime</code>, serving as an upper bound for generated <code>DateTime</code>s. Can be negative, denoting an offset <i>before</i> the provided <code>DateTime</code>.
    * @return A <code>DateTime</code> generator for <code>DateTime</code>s within the expected range.
    */
  def genDateTimeWithinPeriod(dateTime: DateTime, period: ReadablePeriod): Gen[DateTime] = {
    val diffMillis = dateTime.plus(period).getMillis() - dateTime.getMillis()
    Gen.choose(0L min diffMillis, 0L max diffMillis).map(millis => dateTime.plus(millis))
  }
}

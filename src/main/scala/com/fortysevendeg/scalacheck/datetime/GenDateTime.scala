package com.fortysevendeg.scalacheck.datetime

import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary

import org.joda.time._

/**
  * Some generators for working with dates and times
  */
object GenDateTime {

  val genYearsPeriod: Gen[Years] = Gen.choose(-292275054, 292278993).map(Years.ZERO.plus(_)) // Years.MIN_VALUE produces exception-throwing results
  val genMonthsPeriod: Gen[Months] = Gen.choose(Months.MIN_VALUE.getMonths, Months.MAX_VALUE.getMonths).map(Months.ZERO.plus(_))
  val genWeeksPeriod: Gen[Weeks] = Gen.choose(Weeks.MIN_VALUE.getWeeks, Weeks.MAX_VALUE.getWeeks).map(Weeks.ZERO.plus(_))
  val genDaysPeriod: Gen[Days] = Gen.choose(Days.MIN_VALUE.getDays, Days.MAX_VALUE.getDays).map(Days.ZERO.plus(_))
  val genHoursPeriod: Gen[Hours] = Gen.choose(Hours.MIN_VALUE.getHours, Hours.MAX_VALUE.getHours).map(Hours.ZERO.plus(_))
  val genMinutesPeriod: Gen[Minutes] = Gen.choose(Minutes.MIN_VALUE.getMinutes, Minutes.MAX_VALUE.getMinutes).map(Minutes.ZERO.plus(_))
  val genSecondsPeriod: Gen[Seconds] = Gen.choose(Seconds.MIN_VALUE.getSeconds, Seconds.MAX_VALUE.getSeconds).map(Seconds.ZERO.plus(_))

  val genPeriod: Gen[ReadablePeriod] = Gen.oneOf(
    genYearsPeriod,
    genMonthsPeriod,
    genWeeksPeriod,
    genDaysPeriod,
    genHoursPeriod,
    genMinutesPeriod,
    genSecondsPeriod
  )

  /**
    * Generates a <code>DateTime</code> between the given <code>dateTime> and the end of the <code>period</code>
    */
  def genDateTimeWithinPeriod(dateTime: DateTime, period: ReadablePeriod): Gen[DateTime] = {
    val diffMillis = dateTime.plus(period).getMillis() - dateTime.getMillis()
    Gen.choose(0L min diffMillis, 0L max diffMillis).map(millis => dateTime.plus(millis))
  }
}

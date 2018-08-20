---
layout: docs
---

# Creating Ranges

```tut:silent
// this is here to remove noisy warnings
import org.scalacheck.Prop.passed
import org.scalacheck.Prop.forAll
import org.joda.time.DateTime
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
```

You can generate date/time instances only within a certain range, using the `genDateTimeWithinRange` in the `GenDateTime` class. The function takes two parameters, the date/time instances as a base from which to generate new date/time instances, and a range for the generated instances. If the range is positive, it will be in the future from the base date/time, negative in the past.

Showing this usage with Joda Time:

```tut:silent
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.instances.joda._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange

val from = new DateTime(2016, 1, 1, 0, 0)
val range = Period.years(1)

val prop = forAll(genDateTimeWithinRange(from, range)) { dt =>
  dt.getYear == 2016
}

prop.check
// + OK, passed 100 tests.
```

# Using Granularity and Ranges Together

As you would expect, it is possible to use the granularity and range concepts together. This example should not show anything surprising by now:

```tut:silent
import com.fortysevendeg.scalacheck.datetime.instances.joda._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange
import com.fortysevendeg.scalacheck.datetime.joda.granularity.days

val from = new DateTime(2016, 1, 1, 0, 0)
val range = Period.years(1)

val prop = forAll(genDateTimeWithinRange(from, range)) { dt =>
  (dt.getYear == 2016) &&
  (dt.getHourOfDay == 0) &&
  (dt.getMinuteOfHour == 0) &&
  (dt.getSecondOfMinute == 0) &&
  (dt.getMillisOfSecond == 0)
}

prop.check
// + OK, passed 100 tests.
```

# Constraining LocalTime and LocalDate
 
For java.time, whereas ZonedDateTime, LocalDateTime and Instant all take an duration as the range value,
it makes no sense constraining a LocalDate with a duration - as this could create boundaries that are not aligned with date. Instead we only use Period
```tut
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateWithinRange
import java.time._
import org.scalacheck.Gen._
import org.scalacheck.Prop._

val from = LocalDate.of(2017, 2, 1)
val range = Period.ofDays(20)

val prop = forAll(genDateWithinRange(from, range)) { d =>
  d.getYear == 2017 &&
  d.getMonthValue == 2 && 
  d.getDayOfMonth >= 1 &&
  d.getDayOfMonth <= 21
}

prop.check
 
```

For LocalTime, adding a duration could cause wrap-around - so adding 2 hours to 23:00 gives 01:00. Instead you provide the lower and upper values of the time band you wish to generate:

```tut
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genTimeBetween
import java.time._
import org.scalacheck.Gen._
import org.scalacheck.Prop._

val from = LocalTime.of(11, 0, 0)
val to = LocalTime.of(13, 0, 0)

val prop = forAll(genTimeBetween(from, to)) { d =>
  (d.isBefore(to) || d.equals(to)) &&
    (d.isAfter(from) || d.equals(from))
  
}

prop.check
```
  
  
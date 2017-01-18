---
layout: docs
---

# Creating Ranges

```tut:invisible
// this is here to remove noisy warnings
import org.scalacheck.Prop.passed
import org.scalacheck.Prop.forAll
import org.joda.time.DateTime
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
```

You can generate date/time instances only within a certain range, using the `genDateTimeWithinRange` in the `GenDateTime` class. The function takes two parameters, the date/time instances as a base from which to generate new date/time instances, and a range for the generated instances. If the range is positive, it will be in the future from the base date/time, negative in the past.

Showing this usage with Joda Time:

```tut
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.instances.joda._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange

val from = new DateTime(2016, 1, 1, 0, 0)
val range = Period.years(1)

val prop = forAll(genDateTimeWithinRange(from, range)) { dt =>
  dt.getYear == 2016
}

prop.check

```

# Using Granularity and Ranges Together

As you would expect, it is possible to use the granularity and range concepts together. This example should not show anything surprising by now:

```tut
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

```

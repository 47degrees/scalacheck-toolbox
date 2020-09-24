---
layout: docs
---

# Creating Ranges

You can generate date/time instances only within a certain range, using the `genDateTimeWithinRange` in the `GenDateTime` class. The function takes two parameters, the date/time instances as a base from which to generate new date/time instances, and a range for the generated instances. If the range is positive, it will be in the future from the base date/time, negative in the past.

Showing this usage with Joda Time:

```scala mdoc:silent
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.instances.joda._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange
import org.scalacheck.Prop.forAll

val from = new DateTime(2016, 1, 1, 0, 0)
val range = Period.years(1)

forAll(genDateTimeWithinRange(from, range)) { dt =>
  dt.getYear == 2016
}.check()
```

# Using Granularity and Ranges Together

As you would expect, it is possible to use the granularity and range concepts together. This example should not show anything surprising by now:

```scala mdoc:silent
import com.fortysevendeg.scalacheck.datetime.joda.granularity.days

forAll(genDateTimeWithinRange(from, range)) { dt =>
  (dt.getYear == 2016) &&
  (dt.getHourOfDay == 0) &&
  (dt.getMinuteOfHour == 0) &&
  (dt.getSecondOfMinute == 0) &&
  (dt.getMillisOfSecond == 0)
}.check()
```

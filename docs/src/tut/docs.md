---
layout: docs
---

# Get started

The motivation behind this library is to provide a simple, easy way to provide generated date and time instances that are useful to your own domain.

Any issues, suggestions or criticisms are more than welcome.

For SBT, you can add the dependency to your project's build file:

```scala
resolvers += Resolver.sonatypeRepo("releases")

"com.fortysevendeg" %% "scalacheck-datetime" % "0.1.0" % "test"
```

# Current Status

As of version 0.1.0, the following libraries and classes are supported:

  * [Joda Time](http://www.joda.org/joda-time/): The [`DateTime`](http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html) class, and [`Period`](http://joda-time.sourceforge.net/apidocs/org/joda/time/Period.html) for specifying a range of time.
  * [Java SE 8 Date and Time](http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html): The [`ZonedDateTime`](https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html) and [`Duration`](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) classes.

There is an expectation of including more date/time and range classes before 1.0.

# Usage

To arbitrarily generate dates and times, you need to have the `Arbitrary` in scope for your date/time class. Assuming Joda Time:

```tut:invisible
// this is here to remove noisy warnings
import org.scalacheck.Prop.passed
import org.scalacheck.Prop.forAll
import org.joda.time.DateTime
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._

val prop = forAll { dt: DateTime =>
  // some calculations here using the dt parameter
  passed
}

import org.joda.time._
val from = new DateTime(2016, 1, 1, 0, 0)
val range = Period.years(1)

```

```tut
import org.scalacheck.Prop.forAll
import org.joda.time.DateTime
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._

val prop = forAll { dt: DateTime =>
  // some calculations here using the dt parameter
  passed
}

prop.check
```

## A note on imports

For all of the examples given in this document, you can substitute `jdk8` for `joda` and vice-versa, depending on which library you would like to generate instances for.

## Implementation

The infrastructure behind the generation of date/time instances for _any given date/time library_, which may take ranges into account, is done using a fairly simple typeclass, which has the type signature `ScalaCheckDateTimeInfra[D, R]`. That is to say, as long as there is an implicit `ScalaCheckDateTimeInfra` instance in scope for a given date/time type `D` (such as Joda's `DateTime`) and a range type `R` (such as Joda's `Period`), then the code will compile and be able to provide generated date/time instances.

As stated, currently there are two instances, `ScalaCheckDateTimeInfra[DateTime, Period]` for Joda Time and `ScalaCheckDateTimeInfra[ZonedDateTime, Duration]` for Java SE 8's Date and Time.

# Granularity

If you wish to restrict the precision of the generated instances, this library refers to that as _granularity_.

You can constrain the granularity to:

  * Seconds
  * Minutes
  * Hours
  * Days
  * Years

When a value is constrained, the time fields are set to zero, and the rest to the first day of the month, or day of the year. For example, if you constrain a field to be years, the generated instance will be midnight exactly, on the first day of January.

To constrain a generated type, you simply need to provide an import for the typeclass for your date/time and range, and also an import for the granularity. As an example, this time using Java SE 8's `java.time` package:

```tut
import java.time._
import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.years

val prop = forAll { zdt: ZonedDateTime =>
  (zdt.getMonth == Month.JANUARY)
  (zdt.getDayOfMonth == 1) &&
  (zdt.getHour == 0) &&
  (zdt.getMinute == 0) &&
  (zdt.getSecond == 0) &&
  (zdt.getNano == 0)
}

prop.check
```

# Creating Ranges

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

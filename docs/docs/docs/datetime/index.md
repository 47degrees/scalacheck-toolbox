---
layout: docs
---

# Current Status

As of version 0.2.1, the following libraries and classes are supported:

  * [Joda Time](http://www.joda.org/joda-time/): The [`DateTime`](http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html) class, and [`Period`](http://joda-time.sourceforge.net/apidocs/org/joda/time/Period.html) for specifying a range of time.
  * [Java SE 8 Date and Time](http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html): The [`ZonedDateTime`](https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html) and [`Duration`](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) classes.

There is an expectation of including more date/time and range classes before 1.0.

# Usage

To arbitrarily generate dates and times, you need to have the `Arbitrary` in scope for your date/time class. Assuming Joda Time:

```scala mdoc:silent
import org.scalacheck.Prop.passed
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

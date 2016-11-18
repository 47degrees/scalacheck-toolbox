---
layout: docs
---

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

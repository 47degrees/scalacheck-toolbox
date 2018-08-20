---
layout: docs
---
# Granularity

```tut:silent
// this is here to remove noisy warnings
import org.scalacheck.Prop.passed
import org.scalacheck.Prop.forAll
import org.joda.time.DateTime
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
```

If you wish to restrict the precision of the generated instances, this library refers to that as _granularity_.

You can constrain the granularity to:

  * Seconds
  * Minutes
  * Hours
  * Days
  * Years

When a value is constrained, the time fields are set to zero, and the rest to the first day of the month, or day of the year. For example, if you constrain a field to be years, the generated instance will be midnight exactly, on the first day of January.

To constrain a generated type, you simply need to provide an import for the typeclass for your date/time and range, and also an import for the granularity. As an example, this time using Java SE 8's `java.time` package:

```tut:silent
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
// + OK, passed 100 tests.
```

Note that the granularity of Instant not strictly valid as Instant does not really support the notion. However for convenience a Instants can be constrained as for the other types but is performed by transforming the Instant to the ZonedDateTime for UTC, constraining that and converting back again afternwards.  
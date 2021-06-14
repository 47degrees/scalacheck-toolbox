---
layout: docs
---
# YearRange

```scala mdoc:silent
// this is here to remove noisy warnings
import org.scalacheck.Prop.forAll
```

By default, generated date-times are within a large subset of possible values, randing from the year -292278993 to 292278993.
This is perhaps not very realistic, and it can sometimes give you dates and times, including time-zone switches, that may not make much sense today.
We have provided a mechanism to restrict the years generated to a range that you find most comfortable, as `YearRange`.

For example, lets create a property where all generated `ZonedDateTimes` are between the years 1970 and 2100:

```scala mdoc:silent
import java.time._
import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import com.fortysevendeg.scalacheck.datetime.YearRange

implicit val range: YearRange = YearRange.epochTo(2100)

val prop = forAll { (zdt: ZonedDateTime) =>
  zdt.getYear >= 1970 && zdt.getYear <= 2100
}

prop.check()
```

You can create your own range with `YearRange.between(min, max)` as well as using `epochTo`, which defaults the min to 1970.
You cannot specify a `YearRange` when using `GenDateTime.genDateTimeWithinRange`, as that already implies you are using a much more granular range.
This implicit is to be used for generally restricting generated output so that it is "more realistic", not if you need precise control over the ranges, in which case you should use `genDateTimeWithinRange` as mentioned.


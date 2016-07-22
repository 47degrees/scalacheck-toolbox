---
layout: home
technologies:
 - scala: ["ScalaCheck", "This library is for use with ScalaCheck: the property-based testing framework for Scala."]
 - cats: ["Typelevel", "ScalaCheck is part of Typelevel.scala: supplements to the standard library, and much more."]
 - fp: ["Functional Programming", "scalacheck-datetime aims to make functional programming easier to understand and more accessible to all."]
---

```tut:invisible
// This is here to remove the noisy warnings that appear on first run
import org.scalacheck.Arbitrary.arbitrary
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
arbitrary[DateTime].sample
```

# A helping hand for creating sensible generated data for a given range and precision
  When testing dates and times, sometimes you want to limit the test data to a certain range of times, and sometimes you only care about a certain level of precision. This lightweight library aims to provide both of these with ease.

### Installation

```scala
"com.fortysevendeg" %% "scalacheck-datetime" % "0.1-SNAPSHOT"
```


### Works for both Joda Time and Java 8's java.time APIs
  The scalacheck-datetime library is configured to work with existing date and time libraries with ease. `Arbitrary` instances can be pulled into scope with the right import.

```tut
import org.scalacheck.Arbitrary.arbitrary
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
arbitrary[DateTime].sample
```


### Generate date time instances within a given range
  Rather than generating dates and times ranging from both prehistoric times and dates way into the future, you can specify a range to build your instances.

```tut
import org.scalacheck.Arbitrary.arbitrary
import java.time._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
val now = ZonedDateTime.now
val generator = genDateTimeWithinRange(now, Duration.ofDays(7))
(1 to 3).foreach(_ => println(generator.sample))
```


### Generate date time instances to a given precision
  Depending on your use-case, you may not care about generating many different instances at the per-millisecond level. This library lets you specify the granularity of your generated instances. You can ask for seconds, minutes, days, months or years. And, of course, this can be paired up with the generator for ranges, too. 

```tut
import org.scalacheck.Arbitrary.arbitrary
import java.time._
import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.days
val now = ZonedDateTime.now
val generator = genDateTimeWithinRange(now, Duration.ofDays(100))
(1 to 3).foreach(_ => println(generator.sample))
```

Check out the [documentation](/scalacheck-datetime/docs) for more tips on how to get the best from these tools.
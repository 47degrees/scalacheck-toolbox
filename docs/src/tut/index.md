---
layout: home
technologies:
 - scala: ["Scala", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - scalacheck: ["ScalaCheck", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - typelevel: ["Typelevel", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
---


* ## Works for both Joda Time and Java 8's java.time APIs
  The scalacheck-datetime library is configured to work with existing date and time libraries with ease. `Arbitrary` instances can be pulled into scope with the right import.

```tut
import org.scalacheck.Arbitrary.arbitrary
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
arbitrary[DateTime].sample
```


* ## Generate DateTime instances within a given range
  Rather than generating dates and times ranging from both prehistoric times and dates way into the future, you can specify a range to build your instances.

```scala
import org.scalacheck.Arbitrary.arbitrary
import java.time._
import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
val now = ZonedDateTime.now
val generator = genDateTimeWithinRange(now, Duration.ofDays(7))
(1 to 3).foreach(_ => println(generator.sample))
```


* ## Generate DateTime instances to a given precision
  Depending on your use-case, you may not care about generating many different instances at the per-millisecond level. This library lets you specify the granularity of your generated instances. You can ask for seconds, minutes, days, months or years. And, of course, this can be paired up with the generator for ranges, too. Check out the [documentation](/docs) for more tips on how to get the best from these tools.

```tut
import org.scalacheck.Arbitrary.arbitrary
import java.time._
import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.days
val now = ZonedDateTime.now
val generator = genDateTimeWithinRange(now, Duration.ofDays(100))
(1 to 3).foreach(_ => println(generator.sample))
```
---
layout: home
technologies:
 - scala: ["Scala", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - scalacheck: ["ScalaCheck", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - typelevel: ["Typelevel", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
---


* ## Works for both Joda Time and Java 8's java.time APIs
  The scalacheck-datetime library is configured to work with existing date and time libraries with ease. `Arbitrary` instances can be pulled into scope with the right import.

* ```scala
scala> import org.scalacheck.Arbitrary.arbitrary
     import org.scalacheck.Arbitrary.arbitrary
     scala> import org.joda.time._
     import org.joda.time._

     scala> import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
     import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._

     scala> arbitrary[DateTime].sample
     res0: Option[org.joda.time.DateTime] = Some(88317541-09-19T12:25:32.330+01:00)
```


* ## Generate DateTime instances within a given range
  Rather than generating dates and times ranging from both prehistoric times and dates way into the future, you can specify a range to build your instances.

* ```scala
scala> import org.scalacheck.Arbitrary.arbitrary, java.time._
      import org.scalacheck.Arbitrary.arbitrary
      import java.time._
      
      scala> import com.fortysevendeg.scalacheck.datetime.GenDateTime._
      import com.fortysevendeg.scalacheck.datetime.GenDateTime._
      
      scala> import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
      import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
      
      scala> val now = ZonedDateTime.now
      now: java.time.ZonedDateTime = 2016-07-19T16:30:51.742+01:00[Europe/London]
      
      scala> val generator = genDateTimeWithinRange(now, Duration.ofDays(7))
      generator: org.scalacheck.Gen[java.time.ZonedDateTime] = org.scalacheck.Gen$$anon$5@7ee3310
      
      scala> (1 to 3).foreach(_ => println(generator.sample))
      Some(2016-07-25T13:39:48.456+01:00[Europe/London])
      Some(2016-07-26T10:11:27.088+01:00[Europe/London])
      Some(2016-07-24T03:26:10.990+01:00[Europe/London])
```


* ## Generate DateTime instances to a given precision
  Depending on your use-case, you may not care about generating many different instances at the per-millisecond level. This library lets you specify the granularity of your generated instances. You can ask for seconds, minutes, days, months or years. And, of course, this can be paired up with the generator for ranges, too. Check out the [documentation](/docs) for more tips on how to get the best from these tools.

* ```scala
scala> import org.scalacheck.Arbitrary.arbitrary, java.time._
      import org.scalacheck.Arbitrary.arbitrary
      import java.time._
      
      scala> import com.fortysevendeg.scalacheck.datetime.GenDateTime._
      import com.fortysevendeg.scalacheck.datetime.GenDateTime._
      
      scala> import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
      import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
      
      scala> import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.days
      import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.days
      
      scala> val now = ZonedDateTime.now
      now: java.time.ZonedDateTime = 2016-07-19T16:43:11.665+01:00[Europe/London]
      
      scala> val generator = genDateTimeWithinRange(now, Duration.ofDays(100))
      generator: org.scalacheck.Gen[java.time.ZonedDateTime] = org.scalacheck.Gen$$anon$5@14fcd3a3
      
      scala> (1 to 3).foreach(_ => println(generator.sample))
      Some(2016-10-07T00:00+01:00[Europe/London])
      Some(2016-09-18T00:00+01:00[Europe/London])
      Some(2016-10-20T00:00+01:00[Europe/London])
```
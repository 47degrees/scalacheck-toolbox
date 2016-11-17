---
layout: docs
---

# Get started

The motivation behind this library is to provide a simple, easy way to provide generated date and time instances that are useful to your own domain.

Any issues, suggestions or criticisms are more than welcome.

For SBT, you can add the dependency to your project's build file:

```scala
resolvers += Resolver.sonatypeRepo("releases")

"com.fortysevendeg" %% "scalacheck-datetime" % "0.2.0" % "test"
```

# Current Status

As of version 0.2.0, the following libraries and classes are supported:

  * [Joda Time](http://www.joda.org/joda-time/): The [`DateTime`](http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html) class, and [`Period`](http://joda-time.sourceforge.net/apidocs/org/joda/time/Period.html) for specifying a range of time.
  * [Java SE 8 Date and Time](http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html): The [`ZonedDateTime`](https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html) and [`Duration`](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) classes.

There is an expectation of including more date/time and range classes before 1.0.

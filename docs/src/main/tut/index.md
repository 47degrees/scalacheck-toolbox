---
layout: home
technologies:
 - scala: ["ScalaCheck", "This library is for use with ScalaCheck: the property-based testing framework for Scala."]
 - cats: ["Typelevel", "ScalaCheck is part of Typelevel.scala: supplements to the standard library, and much more."]
 - fp: ["Functional Programming", "scalacheck-toolbox aims to make functional programming easier to understand and more accessible to all."]
---

```tut:invisible
// This is here to remove the noisy warnings that appear on first run
import org.scalacheck.Arbitrary.arbitrary
import org.joda.time._
import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda._
arbitrary[DateTime].sample
```

# A helping hand for generating sensible data with ScalaCheck
The ScalaCheck Toolbox is intended to be a set of libraries that can help rein in the power of ScalaCheck in a sensible way, while not impeding your tests. There are three libraries to help you:

  * [`datetime`](/scalacheck-toolbox/docs/datetime/): Limit the test data to a certain range of times, and constrain generation to a certain level of precision.
  * [`magic`](/scalacheck-toolbox/docs/magic/): Enhance the provided generators with some values that are often used to signal danger, or perhaps something more sinister, such as the Strings "_null_", "_False_" or "_Robert'); DROP TABLE Students;--_".
  * [`combinators`](/scalacheck-toolbox/docs/combinators): Provide some useful combinators of generators, such as the pairing of a map _and a list of values that are present in the map_.


View the [documentation](/scalacheck-toolbox/docs) for more tips on how to get the best from these tools.

## Commercial Support

47 Degrees offers commercial support for the scalacheck-toolbox library and associated technologies. To find out more, visit [47 Degrees' Open Source Support](https://www.47deg.com/services/open-source-support/).
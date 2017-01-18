scalacheck-toolbox
====

[![Build Status](https://travis-ci.org/47deg/scalacheck-toolbox.svg?branch=master)](https://travis-ci.org/47deg/scalacheck-toolbox)
[![codecov.io](http://codecov.io/github/47deg/scalacheck-toolbox/coverage.svg?branch=master)](http://codecov.io/github/47deg/scalacheck-toolbox?branch=master)

# A helping hand for generating sensible data with ScalaCheck
The ScalaCheck Toolbox is intended to be a set of libraries that can help rein in the power of ScalaCheck in a sensible way, while not impeding your tests. There are three libraries to help you:

  * [`datetime`](https://47deg.github.io/scalacheck-toolbox/docs/datetime/): Limit the test data to a certain range of times, and constrain generation to a certain level of precision.
  * [`magic`](https://47deg.github.io/scalacheck-toolbox/docs/magic/): Enhance the provided generators with some values that are often used to signal danger, or perhaps something more sinister, such as the Strings "_null_", "_False_" or "_Robert'); DROP TABLE Students;--_".
  * [`combinators`](https://47deg.github.io/scalacheck-toolbox/docs/combinators): Provide some useful combinators of generators, such as the pairing of a map _and a list of values that are present in the map_.


View the [documentation](https://47deg.github.io/scalacheck-toolbox/docs) for more tips on how to get the best from these tools.

---

This project is licenced under the terms of the [Apache Licence, Version 2.0](LICENCE.txt)
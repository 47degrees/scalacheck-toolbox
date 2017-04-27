
[comment]: # (Start Badges)

[![Build Status](https://travis-ci.org/47deg/scalacheck-toolbox.svg?branch=master)](https://travis-ci.org/47deg/scalacheck-toolbox) [![codecov.io](http://codecov.io/github/47deg/scalacheck-toolbox/coverage.svg?branch=master)](http://codecov.io/github/47deg/scalacheck-toolbox?branch=master) [![Maven Central](https://img.shields.io/badge/maven%20central-0.2.2-green.svg)](https://oss.sonatype.org/#nexus-search;gav~com.47deg~scalacheck-toolbox*) [![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/47deg/scalacheck-toolbox/master/LICENSE) [![Latest version](https://img.shields.io/badge/scalacheck--toolbox-0.2.2-green.svg)](https://index.scala-lang.org/47deg/scalacheck-toolbox) [![GitHub Issues](https://img.shields.io/github/issues/47deg/scalacheck-toolbox.svg)](https://github.com/47deg/scalacheck-toolbox/issues)

[comment]: # (End Badges)

scalacheck-toolbox
====

# A helping hand for generating sensible data with ScalaCheck
The ScalaCheck Toolbox is intended to be a set of libraries that can help rein in the power of ScalaCheck in a sensible way, while not impeding your tests. There are three libraries to help you:

  * [`datetime`](https://47deg.github.io/scalacheck-toolbox/docs/datetime/): Limit the test data to a certain range of times, and constrain generation to a certain level of precision.
  * [`magic`](https://47deg.github.io/scalacheck-toolbox/docs/magic/): Enhance the provided generators with some values that are often used to signal danger, or perhaps something more sinister, such as the Strings "_null_", "_False_" or "_Robert'); DROP TABLE Students;--_".
  * [`combinators`](https://47deg.github.io/scalacheck-toolbox/docs/combinators): Provide some useful combinators of generators, such as the pairing of a map _and a list of values that are present in the map_.


View the [documentation](https://47deg.github.io/scalacheck-toolbox/docs) for more tips on how to get the best from these tools.

## scalacheck-toolbox in the wild not found

If you wish to add your library here please consider a PR to include it in the list below.

[comment]: # (Start Copyright)
# Copyright

scalacheck-toolbox is designed and developed by 47 Degrees

Copyright (C) 2016-2017 47 Degrees. <http://47deg.com>

[comment]: # (End Copyright)
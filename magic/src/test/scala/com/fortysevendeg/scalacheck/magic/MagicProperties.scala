/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.magic

import org.scalacheck._
import org.scalacheck.Prop._

import Magic._

object MagicProperties extends Properties("Magic Generators") {

  property("magic strings are generated correctly") = forAll(magicStrings) { s =>
    strings.contains(s)
  }

  property("magic strings are included in full arbitrary generation") = exists { s: String =>
    strings.contains(s)
  }

  property("regular arbitrary strings are included in full arbitrary generation") = exists { s: String =>
    !strings.contains(s)
  }
}

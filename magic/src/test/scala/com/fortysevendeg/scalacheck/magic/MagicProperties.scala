/*
 * scalacheck-datetime
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.magic

import org.scalacheck._
import org.scalacheck.Prop._

object MagicProperties extends Properties("Magic Generators") {

  property("this should not hold") = forAll { _: Int =>
    false
  }
}

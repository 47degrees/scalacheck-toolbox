/*
 * scalacheck-datetime
 * Copyright (C) 2016 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.combinators

import org.scalacheck._
import org.scalacheck.Prop._

object CombinatorProperties extends Properties("Combinator Generators") {

  property("this should not hold") = forAll { _ : Int =>
    false
  }
}

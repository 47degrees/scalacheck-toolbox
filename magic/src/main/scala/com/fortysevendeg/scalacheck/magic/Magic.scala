/*
 * scalacheck-datetime
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.magic

import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary

object Magic {

  private[magic] val strings = List("This", "is", "a", "temporary", "list", "of", "strings")

  val magicStrings: Gen[String] = oneOf(strings)

  implicit val enhancedStringArbitrary: Arbitrary[String] = Arbitrary {
    oneOf(arbitrary[List[Char]] map (_.mkString), magicStrings)
  }
}

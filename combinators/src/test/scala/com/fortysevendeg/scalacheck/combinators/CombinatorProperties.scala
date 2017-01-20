/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.combinators

import org.scalacheck._
import org.scalacheck.Prop._

import Combinators._

object CombinatorProperties extends Properties("Combinator Generators") {

  property("genPickFromMapWithSuccessAndFailure should return a list of elements in the map, and a list of elements not in the map") =
    forAll(genPickFromMapWithSuccessAndFailure[String, String]) { case (map, succs, fails) =>
      succs.forall(s => map.get(s).isDefined) &&
      fails.forall(f => map.get(f).isEmpty)
    }

  property("genPickFromMapWithSuccess should return a list of elements in the map, and a list of elements not in the map") =
    forAll(genPickFromMapWithSuccess[String, String]) { case (map, succs) => succs.forall(s => map.get(s).isDefined) }
}

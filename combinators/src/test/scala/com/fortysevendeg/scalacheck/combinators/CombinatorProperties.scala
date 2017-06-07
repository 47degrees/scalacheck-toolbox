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

  property("genOrderedPair should return a pair with the first element less than the second") =
    forAll(genOrderedPair[Int]) { case (lower, upper) => lower <= upper }

  property("genDistinctTuple should return a pair where the values are never equal") =
    forAll(genDistinctPair[Int]) { case (fst, snd) => fst != snd }

  property("genOrderedList should return a list in ascending order") =
    forAll(genOrderedList[Int]) { list: List[Int] =>
      list.sliding(2, 1).forall {
        case h :: t :: Nil => h <= t
        case _ :: Nil => true
        case _ => false
      }
    }

  property("genOrderedList produces empty lists") = exists(genOrderedList[Int]) { _.isEmpty }

  property("genOrderedList produces single-element lists") = exists(genOrderedList[Int]) { _.length == 1 }

  property("genOrderedList produces lists with more than one element") = exists(genOrderedList[Int]) { _.length > 1 }
}

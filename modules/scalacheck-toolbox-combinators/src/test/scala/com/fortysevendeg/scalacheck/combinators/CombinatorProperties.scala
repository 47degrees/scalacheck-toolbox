/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.scalacheck.combinators

import org.scalacheck.Prop._

import Combinators._

object CombinatorProperties extends BaseCombinatorProperties {

  property(
    "genPickFromMapWithSuccessAndFailure should return a list of elements in the map, and a list of elements not in the map"
  ) = forAll(genPickFromMapWithSuccessAndFailure[String, String]) { case (map, succs, fails) =>
    succs.forall(s => map.get(s).isDefined) &&
    fails.forall(f => map.get(f).isEmpty)
  }

  property(
    "genPickFromMapWithSuccess should return a list of elements in the map, and a list of elements not in the map"
  ) = forAll(genPickFromMapWithSuccess[String, String]) { case (map, succs) =>
    succs.forall(s => map.get(s).isDefined)
  }

  property("genOrderedPair should return a pair with the first element less than the second") =
    forAll(genOrderedPair[Int]) { case (lower, upper) => lower <= upper }

  property("genDistinctTuple should return a pair where the values are never equal") =
    forAll(genDistinctPair[Int]) { case (fst, snd) => fst != snd }

  property("genOrderedList should return a list in ascending order") = forAll(genOrderedList[Int]) {
    (list: List[Int]) =>
      list.sliding(2, 1).forall {
        case h :: t :: Nil => h <= t
        case _ :: Nil      => true
        case _             => false
      }
  }

  property("genOrderedList produces empty lists") = exists(genOrderedList[Int])(_.isEmpty)

  property("genOrderedList produces single-element lists") = exists(genOrderedList[Int]) {
    _.length == 1
  }

  property("genOrderedList produces lists with more than one element") =
    exists(genOrderedList[Int])(_.length > 1)
}

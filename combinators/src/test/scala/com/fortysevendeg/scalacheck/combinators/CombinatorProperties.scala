/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

import org.scalacheck._
import org.scalacheck.Prop._

import Combinators._

object CombinatorProperties extends Properties("Combinator Generators") {

  property(
    "genPickFromMapWithSuccessAndFailure should return a list of elements in the map, and a list of elements not in the map") =
    forAll(genPickFromMapWithSuccessAndFailure[String, String]) {
      case (map, succs, fails) =>
        succs.forall(s => map.get(s).isDefined) &&
          fails.forall(f => map.get(f).isEmpty)
    }

  property(
    "genPickFromMapWithSuccess should return a list of elements in the map, and a list of elements not in the map") =
    forAll(genPickFromMapWithSuccess[String, String]) {
      case (map, succs) => succs.forall(s => map.get(s).isDefined)
    }
}

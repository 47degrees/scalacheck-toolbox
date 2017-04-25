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
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary

object Combinators {

  def genPickFromMapWithSuccessAndFailure[A, B](
      implicit arbA: Arbitrary[A],
      arbB: Arbitrary[B]): Gen[(Map[A, B], List[A], List[A])] =
    for {
      map <- arbitrary[Map[A, B]]
      keys = map.keySet
      validPicks  <- someOf(keys)
      anotherList <- listOf(arbitrary[A])
      invalidPicks = anotherList.filterNot(i => keys.contains(i))
    } yield (map, validPicks.toList, invalidPicks)

  def genPickFromMapWithSuccess[A, B](
      implicit arbA: Arbitrary[A],
      arbB: Arbitrary[B]): Gen[(Map[A, B], List[A])] =
    genPickFromMapWithSuccessAndFailure[A, B].map { case (m, s, _) => (m, s) }

}

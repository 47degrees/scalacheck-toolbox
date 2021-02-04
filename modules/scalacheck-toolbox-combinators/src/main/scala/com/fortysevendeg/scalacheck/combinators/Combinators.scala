/*
 * Copyright 2016-2021 47 Degrees Open Source <https://www.47deg.com>
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

  def genPickFromMapWithSuccessAndFailure[A, B](implicit
      arbA: Arbitrary[A],
      arbB: Arbitrary[B]
  ): Gen[(Map[A, B], List[A], List[A])] =
    for {
      map <- arbitrary[Map[A, B]]
      keys = map.keySet
      validPicks  <- someOf(keys)
      anotherList <- listOf(arbitrary[A])
      invalidPicks = anotherList.filterNot(i => keys.contains(i))
    } yield (map, validPicks.toList, invalidPicks)

  def genPickFromMapWithSuccess[A, B](implicit
      arbA: Arbitrary[A],
      arbB: Arbitrary[B]
  ): Gen[(Map[A, B], List[A])] =
    genPickFromMapWithSuccessAndFailure[A, B].map { case (m, s, _) => (m, s) }

  def genOrderedPair[A](implicit ord: Ordering[A], arb: Arbitrary[A]): Gen[(A, A)] =
    for {
      one <- arbitrary[A]
      two <- arbitrary[A]
    } yield (ord.min(one, two), ord.max(one, two))

  def genDistinctPair[A](implicit ord: Ordering[A], arb: Arbitrary[A]): Gen[(A, A)] =
    for {
      one      <- arbitrary[A]
      maybeTwo <- arbitrary[A]
      two      <- if (!ord.equiv(one, maybeTwo)) Gen.const(maybeTwo) else Gen.fail
    } yield (one, two)

  def genOrderedList[A](implicit ord: Ordering[A], arb: Arbitrary[A]): Gen[List[A]] = {
    /*
     * This function adds an element to the list head that is less than its previous head.
     * So it is, in effect a list of elements in ascending order.
     *
     * By passing toAdd as a parameter, rather than already on the list, you effectively
     * treat the two parameters as a poor-man's non-empty list.
     *
     * You therefore get around having to write code like this when comparing with the previous element:
     * ord.gteq(list.headOption.getOrElse(throw new Exception("This should not happen")), next).
     *
     * This also gets around the need for a "bounded" typeclass, as you always have a minimum element.
     */
    def nextForList(toAdd: A, list: List[A]): Gen[List[A]] =
      for {
        next      <- arbitrary[A]
        validNext <- if (ord.lteq(toAdd, next)) Gen.fail else Gen.const(next)
        more      <- arbitrary[Boolean]
        newList = toAdd :: list
        full <- if (more) nextForList(validNext, newList) else Gen.const(newList)
      } yield full

    for {
      empty    <- arbitrary[Boolean]
      head     <- arbitrary[A]
      toReturn <- if (empty) Gen.const(Nil) else nextForList(head, Nil)
    } yield toReturn
  }
}

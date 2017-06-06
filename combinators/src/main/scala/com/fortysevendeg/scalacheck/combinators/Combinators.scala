/*
 * scalacheck-toolbox
 * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.combinators

import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary

object Combinators {

  def genPickFromMapWithSuccessAndFailure[A, B](implicit arbA: Arbitrary[A], arbB: Arbitrary[B]): Gen[(Map[A, B], List[A], List[A])] = for {
    map <- arbitrary[Map[A, B]]
    keys = map.keySet
    validPicks <- someOf(keys)
    anotherList <- listOf(arbitrary[A])
    invalidPicks = anotherList.filterNot(i => keys.contains(i))
  } yield (map, validPicks.toList, invalidPicks)

  def genPickFromMapWithSuccess[A, B](implicit arbA: Arbitrary[A], arbB: Arbitrary[B]): Gen[(Map[A, B], List[A])] =
    genPickFromMapWithSuccessAndFailure[A, B].map { case (m, s, _) => (m, s) }

  def genOrderedPair[A](implicit ord: Ordering[A], arb: Arbitrary[A]): Gen[(A, A)] = for {
    one <- arbitrary[A]
    two <- arbitrary[A]
  } yield (ord.min(one, two), ord.max(one, two))

  def genDistinctPair[A](implicit ord: Ordering[A], arb: Arbitrary[A]): Gen[(A, A)] = for {
    one <- arbitrary[A]
    maybeTwo <- arbitrary[A]
    two <- if (!ord.equiv(one, maybeTwo)) Gen.const(maybeTwo) else Gen.fail
  } yield (one, two)
}

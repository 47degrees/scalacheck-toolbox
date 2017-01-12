/*
 * scalacheck-datetime
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

}

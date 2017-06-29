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

package com.fortysevendeg.scalacheck.magic

import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary

object Magic {

  private[magic] val strings = scala.io.Source
    .fromResource("data.txt")
    .getLines
    .filterNot { s =>
      val trimmed = s.trim
      trimmed.startsWith("#") || trimmed.isEmpty
    }
    .toList

  val magicStrings: Gen[String] = oneOf(strings)

  implicit val enhancedStringArbitrary: Arbitrary[String] = Arbitrary {
    oneOf(arbitrary[List[Char]] map (_.mkString), magicStrings)
  }
}

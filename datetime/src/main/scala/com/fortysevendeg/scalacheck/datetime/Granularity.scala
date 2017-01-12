/*
 * scalacheck-datetime
 * Copyright (C) 2016 47 Degrees, LLC. <http://www.47deg.com>
 */

package com.fortysevendeg.scalacheck.datetime

trait Granularity[A] {
  val normalize: A => A
  val description: String
}

object Granularity {
  implicit def identity[A]: Granularity[A] = new Granularity[A] {
    val normalize = (a: A) => a
    val description = "None"
  }
}

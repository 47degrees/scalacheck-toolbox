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

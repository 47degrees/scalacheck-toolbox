package com.fortysevendeg.scalacheck.testing

import com.fortysevendeg.scalacheck.magic.Magic

import org.scalacheck._
import org.scalacheck.Prop._

object Integration extends Properties("Scalacheck Toolbox JAR build integration tests") {

  property("Summoning a magic string should not result in an exception") = exists(Magic.magicStrings) { _ =>
    passed
  }

}

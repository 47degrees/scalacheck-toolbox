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

import org.scalatest.FunSuite
import java.io.File

class MagicSpec extends FunSuite {

  test("Naughty strings data file should exist in the project") {
    val fullStringsFileName = getClass.getResource(Magic.stringsFileName).getFile

    val asFile = new File(fullStringsFileName)

    assert(
      asFile.exists,
      """Magic Strings file is not present as "resources/blns.txt". Make sure the git submodule has been pulled using <git submodule update --recursive> from the command line"""
    )
  }
}

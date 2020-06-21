import sbt._
import sbt.Keys._

import scala.io.Source

/**
 * This plugin can be used to download the "big list of naughty strings" as
 * a `blns.txt` resource file.
 */
object BigListOfNaughtyStringsPlugin extends AutoPlugin {

  val blnsUrl =
    "https://raw.githubusercontent.com/minimaxir/big-list-of-naughty-strings/894882e7/blns.txt"

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(Compile / resourceGenerators += Def.task {
      val file = (Compile / resourceManaged).value / "blns.txt"

      val source = Source.fromURL(blnsUrl)

      IO.write(file, source.mkString)

      Seq(file)
    }.taskValue)

}

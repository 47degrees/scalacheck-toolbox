import sbt.internal.ProjectMatrix

val scala2_12        = "2.12.12"
val scala2_13        = "2.13.6"
val scala3           = "3.0.1"
val allScalaVersions = List(scala2_12, scala2_13, scala3)

ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := scala3
publish / skip := true

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; ++test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

lazy val microsite = project
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .dependsOn(
    `scalacheck-toolbox-datetime`.jvm(scala2_13),
    `scalacheck-toolbox-magic`.jvm(scala2_13),
    `scalacheck-toolbox-combinators`.jvm(scala2_13)
  )
  .settings(
    publish / skip := true,
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(
      Seq(
        `scalacheck-toolbox-datetime`.jvm(scala2_13),
        `scalacheck-toolbox-magic`.jvm(scala2_13),
        `scalacheck-toolbox-combinators`.jvm(scala2_13)
      ).map(_.project): _*
    )
  )

lazy val documentation = project
  .settings(
    publish / skip := true,
    mdocOut := file(".")
  )
  .enablePlugins(MdocPlugin)

lazy val `scalacheck-toolbox-datetime`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-datetime"))
    .settings(description := "A library for helping use date and time libraries with ScalaCheck")
    .settings(
      libraryDependencies ++= Seq(
        "org.scalacheck"         %%% "scalacheck"              % "1.15.4",
        "org.scala-lang.modules" %%% "scala-collection-compat" % "2.5.0"
      )
    )
    .jvmPlatform(
      scalaVersions = allScalaVersions,
      libraryDependencies += "joda-time" % "joda-time" % "2.10.10"
    )
    .jsPlatform(
      scalaVersions = allScalaVersions,
      settings = Seq(
        libraryDependencies += "io.github.cquiroz" %%% "scala-java-time" % "2.3.0" % Test
      )
    )

lazy val `scalacheck-toolbox-magic`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-magic"))
    .enablePlugins(BigListOfNaughtyStringsPlugin)
    .settings(description := "ScalaCheck Generators for magic values")
    .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.15.4")
    .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `scalacheck-toolbox-combinators`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-combinators"))
    .settings(description := "Useful generic combinators for ScalaCheck")
    .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.15.4")
    .jvmPlatform(scalaVersions = allScalaVersions)
    .jsPlatform(scalaVersions = allScalaVersions)

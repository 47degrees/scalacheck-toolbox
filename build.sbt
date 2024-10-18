import sbt.internal.ProjectMatrix

val scala2_12        = "2.12.19"
val scala2_13        = "2.13.14"
val scala3           = "3.3.3"
val allScalaVersions = List(scala2_12, scala2_13, scala3)

ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := scala2_13
publish / skip           := true

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
    scalacOptions --= Seq("-Werror", "-Xfatal-warnings"),
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(
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
    mdocOut        := file(".")
  )
  .enablePlugins(MdocPlugin)

lazy val `scalacheck-toolbox-datetime`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-datetime"))
    .settings(description := "A library for helping use date and time libraries with ScalaCheck")
    .settings(
      libraryDependencies ++= Seq(
        "org.scalacheck"         %%% "scalacheck"              % "1.18.0",
        "org.scala-lang.modules" %%% "scala-collection-compat" % "2.12.0"
      ),
      scalacOptions --= Seq("-Werror", "-Xfatal-warnings")
    )
    .jvmPlatform(
      scalaVersions = allScalaVersions,
      libraryDependencies += "joda-time" % "joda-time" % "2.13.0"
    )
    .jsPlatform(
      scalaVersions = allScalaVersions,
      settings = Seq(
        libraryDependencies += "io.github.cquiroz" %%% "scala-java-time" % "2.6.0" % Test
      )
    )

lazy val `scalacheck-toolbox-magic`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-magic"))
    .enablePlugins(BigListOfNaughtyStringsPlugin)
    .settings(description := "ScalaCheck Generators for magic values")
    .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.18.0")
    .settings(scalacOptions --= Seq("-Werror", "-Xfatal-warnings"))
    .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `scalacheck-toolbox-combinators`: ProjectMatrix =
  (projectMatrix in file("modules/scalacheck-toolbox-combinators"))
    .settings(description := "Useful generic combinators for ScalaCheck")
    .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.18.0")
    .settings(scalacOptions --= Seq("-Werror", "-Xfatal-warnings"))
    .jvmPlatform(scalaVersions = allScalaVersions)
    .jsPlatform(scalaVersions = allScalaVersions)

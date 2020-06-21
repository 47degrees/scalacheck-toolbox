ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := "2.13.2"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.11", "2.13.2")

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; +test")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "publishMicrosite")

lazy val `scalacheck-toolbox-datetime` = module
  .settings(description := "A library for helping use date and time libraries with ScalaCheck")
  .settings(libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3")
  .settings(libraryDependencies += "joda-time" % "joda-time" % "2.10.6")

lazy val `scalacheck-toolbox-magic` = module
  .enablePlugins(BigListOfNaughtyStringsPlugin)
  .settings(description := "ScalaCheck Generators for magic values")
  .settings(libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3")

lazy val `scalacheck-toolbox-combinators` = module
  .settings(description := "Useful generic combinators for ScalaCheck")
  .settings(libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3")

lazy val docs: Project = (project in file("docs"))
  .settings(micrositeSettings: _*)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .dependsOn(allModules: _*)

lazy val `project-docs` = (project in file(".docs"))
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .enablePlugins(MdocPlugin)

ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1")

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; docs/tut; +test")
addCommandAlias("ci-docs", "project-docs/mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "docs/publishMicrosite")

lazy val `scalacheck-toolbox-datetime` = module
  .settings(description := "A library for helping use date and time libraries with ScalaCheck")
  .settings(commonDeps)

lazy val `scalacheck-toolbox-magic` = module
  .enablePlugins(BigListOfNaughtyStringsPlugin)
  .settings(description := "ScalaCheck Generators for magic values")
  .settings(commonDeps)

lazy val `scalacheck-toolbox-combinators` = module
  .settings(description := "Useful generic combinators for ScalaCheck")
  .settings(commonDeps)

lazy val docs: Project = (project in file("docs"))
  .settings(micrositeSettings: _*)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(allModules.map(_.project): _*)
  )
  .dependsOn(allModules: _*)

lazy val `project-docs` = (project in file(".docs"))
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .enablePlugins(MdocPlugin)

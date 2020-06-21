ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1")

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; docs/tut; +test")
addCommandAlias("ci-docs", "project-docs/mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "docs/publishMicrosite")

lazy val root = (project in file("."))
  .dependsOn(datetime, magic, combinators)
  .aggregate(datetime, magic, combinators)
  .settings(skip in publish := true)

lazy val datetime = (project in file("datetime"))
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-datetime",
      description := "A library for helping use date and time libraries with ScalaCheck"
    )
  )
  .settings(testSettings)
  .settings(commonDeps)

lazy val magic = (project in file("magic"))
  .enablePlugins(BigListOfNaughtyStringsPlugin)
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-magic",
      description := "ScalaCheck Generators for magic values"
    )
  )
  .settings(testSettings)
  .settings(commonDeps)

lazy val combinators = (project in file("combinators"))
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-combinators",
      description := "Useful generic combinators for ScalaCheck"
    )
  )
  .settings(testSettings)
  .settings(commonDeps)

lazy val docs: Project = (project in file("docs"))
  .settings(moduleName := "scalacheck-toolbox-docs")
  .settings(name := "scalacheck-toolbox")
  .settings(micrositeSettings: _*)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(datetime, magic, combinators, docs)
  )
  .dependsOn(datetime)
  .dependsOn(magic)
  .dependsOn(combinators)

lazy val `project-docs` = (project in file(".docs"))
  .dependsOn(datetime, magic, combinators)
  .aggregate(datetime, magic, combinators)
  .settings(moduleName := "scalacheck-toolbox-project-docs")
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .enablePlugins(MdocPlugin)

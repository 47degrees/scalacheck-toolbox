import com.typesafe.sbt.SbtGhPages.ghpages
import com.typesafe.sbt.SbtGit.git
import de.heikoseeberger.sbtheader.HeaderPattern

lazy val commonSettings = Seq(
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  startYear := Option(2016),
  homepage := Option(url("https://47deg.github.io/scalacheck-toolbox/")),
  organizationHomepage := Option(url("http://47deg.com")),
  scalaVersion := "2.11.8"
  crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0"),
  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  headers := Map(
  "scala" -> (
    HeaderPattern.cStyleBlockComment,
      """|/*
         | * scalacheck-toolbox
         | * Copyright (C) 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
         | */
         |
         |""".stripMargin
    )
  )
)

lazy val dependencies = libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4",
  "org.specs2" %% "specs2-core" % "3.8.6" % "test",
  "joda-time" % "joda-time" % "2.9.4"
)

lazy val ghpagesSettings =
  ghpages.settings ++
  Seq(git.remoteRepo := "git@github.com:47deg/scalacheck-toolbox.git")

lazy val docsSettings = Seq(
    micrositeName := "scalacheck-toolbox",
    micrositeGithubRepo := "scalacheck-toolbox",
    micrositeDocumentationUrl := "/scalacheck-toolbox/docs/",
    micrositeBaseUrl := "/scalacheck-toolbox"
  ) ++
  commonSettings ++
  dependencies

lazy val root = (project in file("."))
  .settings(Seq(publishArtifact := false) ++ commonSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .aggregate(datetime, magic, combinators)

lazy val datetime = (project in file("datetime"))
  .settings(Seq(
    moduleName := "scalacheck-toolbox-datetime",
    description := "A library for helping use date and time libraries with ScalaCheck"
  ) ++ commonSettings ++ dependencies)

lazy val magic = (project in file ("magic"))
  .settings(Seq(
    moduleName := "scalacheck-toolbox-magic",
    description := "ScalaCheck Generators for magic values"
  ) ++ commonSettings ++ dependencies)

lazy val combinators = (project in file ("combinators"))
  .settings(Seq(
    moduleName := "scalacheck-toolbox-combinators",
    description := "Useful generic combinators for ScalaCheck"
  ) ++ commonSettings ++ dependencies)

lazy val docs = (project in file("docs"))
  .settings(moduleName := "scalacheck-toolbox-docs")
  .settings(name := "scalacheck-toolbox")
  .settings(docsSettings:_ *)
  .enablePlugins(MicrositesPlugin)
  .dependsOn(datetime)

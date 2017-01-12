import com.typesafe.sbt.SbtGhPages.ghpages
import com.typesafe.sbt.SbtGit.git
import de.heikoseeberger.sbtheader.HeaderPattern

lazy val commonSettings = Seq(
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  version := "0.2.1-SNAPSHOT",
  startYear := Option(2016),
  homepage := Option(url("https://47deg.github.io/scalacheck-datetime/")),
  organizationHomepage := Option(url("http://47deg.com")),
  scalaVersion := "2.12.0",
  crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0"),
  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  headers := Map(
  "scala" -> (
    HeaderPattern.cStyleBlockComment,
      """|/*
         | * scalacheck-datetime
         | * Copyright (C) 2016 47 Degrees, LLC. <http://www.47deg.com>
         | */
         |
         |""".stripMargin
    )
  )
)

lazy val dependencies = libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4",
  "joda-time" % "joda-time" % "2.9.4"
)

lazy val ghpagesSettings =
  ghpages.settings ++
  Seq(git.remoteRepo := "git@github.com:47deg/scalacheck-datetime.git")

lazy val docsSettings = Seq(
    micrositeName := "scalacheck-datetime",
    micrositeGithubRepo := "scalacheck-datetime",
    micrositeDocumentationUrl := "/scalacheck-datetime/docs/",
    micrositeBaseUrl := "/scalacheck-datetime"
  ) ++
  commonSettings ++
  dependencies

lazy val root = (project in file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .aggregate(datetime, magic, combinators)

lazy val datetime = (project in file("datetime"))
  .settings(Seq(
    moduleName := "scalacheck-datetime",
    description := "A library for helping use date and time libraries with ScalaCheck"
  ) ++ commonSettings ++ dependencies)

lazy val magic = (project in file ("magic"))
  .settings(Seq(
    moduleName := "scalacheck-magic",
    description := "ScalaCheck Generators for magic values"
  ) ++ commonSettings ++ dependencies)

lazy val combinators = (project in file ("combinators"))
  .settings(Seq(
    moduleName := "scalacheck-combinators",
    description := "Useful generic combinators for ScalaCheck"
  ) ++ commonSettings ++ dependencies)

lazy val docs = (project in file("docs"))
  .settings(moduleName := "scalacheck-datetime-docs")
  .settings(docsSettings:_ *)
  .enablePlugins(MicrositesPlugin)
  .dependsOn(root)

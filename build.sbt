import com.typesafe.sbt.SbtGhPages.ghpages
import com.typesafe.sbt.SbtGit.git

lazy val buildSettings = Seq(
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  description := "A library for helping use date and time libraries with ScalaCheck",
  startYear := Option(2016),
  homepage := Option(url("http://47deg.github.io/github4s/")),
  organizationHomepage := Option(url("http://47deg.com")),
  scalaVersion := "2.11.8",
  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
)

lazy val dependencies = libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.0",
  "joda-time" % "joda-time" % "2.9.4"
)

lazy val scalacheckDatetimeSettings = buildSettings ++ dependencies

lazy val tutDirectoriesSettings = Seq(
  tutSourceDirectory := sourceDirectory.value / "tut",
  tutTargetDirectory := sourceDirectory.value / "jekyll"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false)

lazy val ghpagesSettings = ghpages.settings ++ Seq(git.remoteRepo := "git@github.com:47deg/scalacheck-datetime.git")

lazy val docsSettings = buildSettings ++ dependencies ++ tutSettings ++ tutDirectoriesSettings ++ noPublishSettings ++ ghpagesSettings

lazy val root = (project in file("."))
  .settings(moduleName := "scalacheck-datetime")
  .settings(scalacheckDatetimeSettings:_ *)

lazy val docs = (project in file("docs"))
  .settings(moduleName := "scalacheck-datetime-docs")
  .settings(docsSettings:_ *)
  .enablePlugins(JekyllPlugin)
  .dependsOn(root)

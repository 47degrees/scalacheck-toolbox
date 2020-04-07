import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys
import scoverage.ScoverageKeys._
import com.alejandrohdezma.sbt.github.SbtGithubPlugin
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import tut.TutPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = SbtGithubPlugin

  object autoImport {

    lazy val V = new {
      val jodaTime: String   = "2.10.5"
      val scalacheck: String = "1.14.3"
      val scala211: String   = "2.11.12"
      val scala212: String   = "2.12.10"
      val scala213: String   = "2.13.1"
    }

    lazy val docsMappingsAPIDir: SettingKey[String] =
      settingKey[String]("Name of subdirectory in site target directory for api docs")

    lazy val noPublishSettings = Seq(
      publish := ((): Unit),
      publishLocal := ((): Unit),
      publishArtifact := false,
      publishMavenStyle := false // suppress warnings about intransitive deps (not published anyway)
    )

    lazy val micrositeSettings = Seq(
      micrositeName := "scalacheck-toolbox",
      micrositeCompilingDocsTool := WithTut,
      micrositeDescription := "A helping hand for generating sensible data with ScalaCheck",
      micrositeDocumentationUrl := "/scalacheck-toolbox/docs/",
      micrositeBaseUrl := "/scalacheck-toolbox",
      micrositeGithubRepo := "scalacheck-toolbox",
      micrositeGithubOwner := "47degrees",
      micrositePushSiteWith := GitHub4s,
      micrositeTheme := "pattern",
      micrositeGithubToken := Option(System.getenv().get("ORG_GITHUB_TOKEN")),
      micrositeCompilingDocsTool := WithTut,
      includeFilter in Jekyll := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md",
      docsMappingsAPIDir in ScalaUnidoc := "api",
      addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), docsMappingsAPIDir in ScalaUnidoc)
    )

    lazy val testSettings = Seq(
      fork in Test := false
    )

    lazy val commonDeps = Seq(
      libraryDependencies ++= Seq(
        "org.scalacheck" %% "scalacheck" % V.scalacheck,
        "joda-time"      % "joda-time"   % V.jodaTime
      )
    )
  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "scalacheck-toolbox",
      startYear := Option(2016),
      organization := "com.47deg",
      organizationName := "47 Degrees",
      organizationHomepage := Some(url("https://www.47deg.com/")),
      crossScalaVersions := Seq(V.scala211, V.scala212, V.scala213),
      homepage := Option(url("https://47degrees.github.io/scalacheck-toolbox/")),
      description := "A helping hand for generating sensible data with ScalaCheck",
      scalacOptions := {
        val scalacOptions213 = scalacOptions.value filterNot Set("-Xfuture").contains
        CrossVersion.partialVersion(scalaBinaryVersion.value) match {
          case Some((2, 13)) => scalacOptions213
          case _             => scalacOptions.value
        }
      }
    )
}

import com.alejandrohdezma.sbt.modules.ModulesPlugin.autoImport.allModules
import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import sbtunidoc.BaseUnidocPlugin.autoImport._
import sbtunidoc.ScalaUnidocPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  object autoImport {

    lazy val docsMappingsAPIDir: SettingKey[String] =
      settingKey[String]("Name of subdirectory in site target directory for api docs")

    lazy val micrositeSettings = Seq(
      micrositeName := "scalacheck-toolbox",
      micrositeDescription := "A helping hand for generating sensible data with ScalaCheck",
      micrositeDocumentationUrl := "/scalacheck-toolbox/docs/",
      micrositeBaseUrl := "/scalacheck-toolbox",
      micrositeGithubRepo := "scalacheck-toolbox",
      micrositeGithubOwner := "47degrees",
      micrositePushSiteWith := GitHub4s,
      micrositeTheme := "pattern",
      micrositeGithubToken := Option(System.getenv().get("GITHUB_TOKEN")),
      micrositeCompilingDocsTool := WithMdoc,
      includeFilter in Jekyll := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md",
      docsMappingsAPIDir in ScalaUnidoc := "api",
      addMappingsToSiteDir(
        mappings in (ScalaUnidoc, packageDoc),
        docsMappingsAPIDir in ScalaUnidoc
      ),
      unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(allModules.map(_.project): _*)
    )

  }

}

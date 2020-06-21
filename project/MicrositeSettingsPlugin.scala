import com.alejandrohdezma.sbt.modules.ModulesPlugin.autoImport.allModules
import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositesPlugin
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbtunidoc.BaseUnidocPlugin.autoImport._
import sbtunidoc.ScalaUnidocPlugin.autoImport._

object MicrositeSettingsPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = MicrositesPlugin

  object autoImport {

    lazy val docsMappingsAPIDir: SettingKey[String] =
      settingKey[String]("Name of subdirectory in site target directory for api docs")

  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      micrositeName := "scalacheck-toolbox",
      micrositeDescription := "A helping hand for generating sensible data with ScalaCheck",
      micrositeDocumentationUrl := "/scalacheck-toolbox/docs/",
      micrositeBaseUrl := "/scalacheck-toolbox",
      micrositePushSiteWith := GitHub4s,
      micrositeTheme := "pattern",
      micrositeGithubToken := Option(System.getenv().get("GITHUB_TOKEN")),
      docsMappingsAPIDir in ScalaUnidoc := "api",
      addMappingsToSiteDir(
        mappings in (ScalaUnidoc, packageDoc),
        docsMappingsAPIDir in ScalaUnidoc
      ),
      unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(allModules.map(_.project): _*)
    )

}

pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

lazy val root = (project in file("."))
  .dependsOn(datetime, magic, combinators)
  .aggregate(datetime, magic, combinators)
  .settings(noPublishSettings: _*)

lazy val datetime = (project in file("datetime"))
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-datetime",
      description := "A library for helping use date and time libraries with ScalaCheck"
    ))
  .settings(testSettings)
  .settings(commonDeps)

lazy val magic = (project in file("magic"))
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-magic",
      description := "ScalaCheck Generators for magic values"
    ))
  .settings(testSettings)
  .settings(commonDeps)

lazy val combinators = (project in file("combinators"))
  .settings(
    Seq(
      moduleName := "scalacheck-toolbox-combinators",
      description := "Useful generic combinators for ScalaCheck"
    ))
  .settings(testSettings)
  .settings(commonDeps)

lazy val docs = (project in file("docs"))
  .settings(moduleName := "scalacheck-toolbox-docs")
  .settings(name := "scalacheck-toolbox")
  .settings(micrositeSettings: _*)
  .enablePlugins(MicrositesPlugin)
  .dependsOn(datetime)

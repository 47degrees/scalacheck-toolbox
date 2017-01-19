organizationName in ThisBuild := "47 Degrees"

organizationHomepage in ThisBuild := Some(new URL("http://47deg.com"))

publishMavenStyle in ThisBuild := true

startYear in ThisBuild := Some(2016)

description in ThisBuild := "A helping hand for generating sensible data with ScalaCheck"

homepage in ThisBuild := Some(url("http://47deg.com"))

scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/47deg/scalacheck-toolbox"), "https://github.com/47deg/scalacheck-toolbox.git"))

licenses in ThisBuild := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

pomExtra in ThisBuild :=
    <developers>
      <developer>
        <name>47 Degrees (twitter: @47deg)</name>
        <email>hello@47deg.com</email>
      </developer>
      <developer>
        <name>47 Degrees</name>
      </developer>
    </developers>

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

lazy val gpgFolder = sys.env.getOrElse("GPG_FOLDER", ".")

pgpPassphrase in ThisBuild := Some(sys.env.getOrElse("GPG_PASSPHRASE", "").toCharArray)

pgpPublicRing in ThisBuild := file(s"$gpgFolder/pubring.gpg")

pgpSecretRing in ThisBuild := file(s"$gpgFolder/secring.gpg")

credentials += Credentials("Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("PUBLISH_USERNAME", ""),
  sys.env.getOrElse("PUBLISH_PASSWORD", ""))

publishArtifact in Test := false

lazy val publishSnapshot = taskKey[Unit]("Publish only if the version is a SNAPSHOT")

publishSnapshot := Def.taskDyn {
  if (isSnapshot.value) Def.task { PgpKeys.publishSigned.value }
  else Def.task(println("Actual version is not a Snapshot. Skipping publish."))
}.value

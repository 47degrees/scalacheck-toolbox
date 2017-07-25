name := "jar-integration"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.47deg" %% "scalacheck-toolbox-magic" % version.value,
  "com.47deg" %% "scalacheck-toolbox-combinators" % version.value,
  "com.47deg" %% "scalacheck-toolbox-datetime" % version.value
)


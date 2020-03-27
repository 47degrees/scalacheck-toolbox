resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.geirsson"  % "sbt-ci-release"   % "1.5.2")
addSbtPlugin("com.47deg"     % "sbt-microsites"   % "1.1.5")
addSbtPlugin("com.47deg"     % "sbt-org-policies" % "0.13.3")
addSbtPlugin("org.scoverage" % "sbt-scoverage"    % "1.6.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt"     % "2.3.2")

---
layout: docs
---

# Get started

Any issues, suggestions or criticisms are more than welcome.

For SBT, you can add the relevant dependency to your project's build file:

```scala
resolvers += Resolver.sonatypeRepo("releases")

"com.47deg" %% "scalacheck-toolbox-datetime" % "@VERSION@" % "test"

"com.47deg" %% "scalacheck-toolbox-magic" % "@VERSION@" % "test"

"com.47deg" %% "scalacheck-toolbox-combinators" % "@VERSION@" % "test"
```

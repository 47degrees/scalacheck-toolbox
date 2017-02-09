#!/bin/bash

if [[ "$TRAVIS_BRANCH" == "master" && $(cat version.sbt) =~ "-SNAPSHOT"]]; then
  sbt ++$TRAVIS_SCALA_VERSION +publish;
fi

name := """playProjectUser"""
organization := "Zilingo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

//libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "zilingo.com.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "zilingo.com.binders._"
// play.sbt.routes.RoutesKeys.routesImport += "zilingo.com.binders._"
// https://mvnrepository.com/artifact/com.typesafe.play/play-ws
libraryDependencies += "com.typesafe.play" %% "play-ws" % "2.5.18" withSources()
// https://mvnrepository.com/artifact/com.typesafe.slick/slick
libraryDependencies ++= Seq("com.typesafe.play" %% "play-slick" % "2.0.0" withSources(),
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0" withSources())
libraryDependencies += "org.postgresql" %"postgresql" %"42.2.1" withSources()
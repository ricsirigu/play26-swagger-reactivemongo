name := "play-swagger-reactivemongo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.12"

val reactiveMongoVer = "1.0.0-play26"

libraryDependencies ++= Seq(
  guice,
  "org.reactivemongo"      %% "play2-reactivemongo" % reactiveMongoVer,
  "io.swagger"             %% "swagger-play2"       % "1.7.1",
  "org.webjars"            %  "swagger-ui"          % "3.22.2",
  "org.scalatestplus.play" %% "scalatestplus-play"  % "5.0.0-M2" % Test
)

import play.sbt.routes.RoutesKeys

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

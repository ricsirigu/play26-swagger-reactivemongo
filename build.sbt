name := "play-swagger-reactivemongo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

val reactiveMongoVer = "0.12.5-play26"

libraryDependencies ++= Seq(
  guice,
  "org.reactivemongo"      %% "play2-reactivemongo" % reactiveMongoVer,
  "io.swagger"             %% "swagger-play2"       % "1.6.0",
  "org.webjars"            %  "swagger-ui"          % "3.1.4",
  "org.scalatestplus.play" %% "scalatestplus-play"  % "3.1.1" % Test
)


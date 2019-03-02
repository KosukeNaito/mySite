name := """eiwaTask"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions
)

libraryDependencies += "postgresql" % "postgresql" % "10.7.jdbc4"

lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

fork in run := true

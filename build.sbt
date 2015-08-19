name := "ScreenStream"

version := "0.0.1"

organization := "ca.marvelmathew"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/content/repositories/releases",
  "spray repo" at "http://repo.spray.io"
)

scalacOptions ++= Seq("-deprecation", "-unchecked")

scalariformSettings

libraryDependencies ++= {
  val sprayVersion = "1.3.3"
  Seq(
    "org.specs2"        %% "specs2"             % "2.3.12"           % "test",
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %% "spray-httpx" % sprayVersion,
    "io.spray" %%  "spray-json" % "1.3.2",
    "com.typesafe.akka" %% "akka-actor" % "2.3.9"
  )
}

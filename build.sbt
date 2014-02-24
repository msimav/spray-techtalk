name := "beer.io"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.2.3",
    "com.typesafe.akka" %% "akka-testkit" % "2.2.3",
    "io.spray" %%  "spray-json" % "1.2.5",
    "io.spray" %  "spray-can"     % "1.2.0",
    "io.spray" %  "spray-routing" % "1.2.0",
    "io.spray" %  "spray-testkit" % "1.2.0",
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "org.mockito" % "mockito-core" % "1.9.0" % "test"
)

name := "SwayDB.examples"

version := "0.1"

scalaVersion := "2.12.7"

resolvers += Opts.resolver.sonatypeSnapshots
resolvers += Opts.resolver.sonatypeReleases

libraryDependencies ++=
  Seq(
    "io.swaydb" %% "swaydb" % "0.6",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "com.typesafe.akka" %% "akka-typed" % "2.5.4",
    "io.monix" %% "monix" % "3.0.0-RC1",
    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
    "ch.qos.logback" % "logback-classic" % "1.2.3" % Test,
    "io.circe" %% "circe-core" % "0.10.0" % Test,
    "io.circe" %% "circe-generic" % "0.10.0" % Test,
    "io.circe" %% "circe-parser" % "0.10.0" % Test

  )
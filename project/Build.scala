import sbt._
import Keys._

object AkkaInstrumentedMailboxBuild extends Build {
  lazy val root = Project(
      id = "akka-instrumented-mailbox",
      base = file("."))
    .settings(
      resolvers += Dependencies.akkaResolver,
      libraryDependencies ++= Dependencies.root,
      unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil,
      unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil)
}

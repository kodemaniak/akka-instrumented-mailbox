import sbt._

object Dependencies {
  val akkaVersion = "2.3.2"

  val akkaResolver = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val metrics = "nl.grons" %% "metrics-scala" % "3.0.5_a2.3"

  val root = Seq(akka, metrics)
}

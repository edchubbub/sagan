val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.1"

val Elastic4sVersion = "7.9.1"

val CirceVersion = "0.12.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % Elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % Elastic4sVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % Elastic4sVersion
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)
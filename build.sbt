val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.1"
val Elastic4sVersion = "7.9.1"

val akkaHttp = Seq(
  "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)

val elastic4s = Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % Elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % Elastic4sVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % Elastic4sVersion
)

libraryDependencies ++= akkaHttp ++ elastic4s

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value / "scalapb"
)

// (optional) If you need scalapb/scalapb.proto or anything from
// google/protobuf/*.proto
libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.0"
)
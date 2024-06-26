val V = new {
  val scala = "3.3.3"

  val http4s = "0.23.26"

  val http4sScalatags = "0.25.2"

  val scalatags = "0.12.0"

  val scribe = "3.13.2"
}

scalaVersion := V.scala

enablePlugins(Smithy4sCodegenPlugin)
enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "com.disneystreaming.smithy4s" %% "smithy4s-http4s" % smithy4sVersion.value,
  "com.disneystreaming.smithy4s" %% "smithy4s-aws-http4s" % smithy4sVersion.value,
  "org.http4s"  %% "http4s-ember-server" % V.http4s,
  "org.http4s"  %% "http4s-ember-client" % V.http4s,
  "org.http4s"  %% "http4s-scalatags"    % V.http4sScalatags,
  "com.lihaoyi" %% "scalatags"           % V.scalatags,
  "com.outr"    %% "scribe-slf4j"        % V.scribe,
  "com.outr"    %% "scribe-cats"         % V.scribe,
  "com.outr"    %% "scribe-json-circe"   % V.scribe,
)

Compile / smithy4sInputDirs += (ThisBuild / baseDirectory).value / "smithy"

// The `AWS` object contains a list of references to artifacts that contain specifications to AWS services.
smithy4sAwsSpecs ++= Seq(AWS.comprehend)

Compile / doc / sources := Nil

run / fork := true

run / envVars += "SMITHY4S_PORT" -> "8080"

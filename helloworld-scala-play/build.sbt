
organization := "Scala & Play Examples for Knative Serving"
name := "helloworld-scala-play"
version := "0.0.1"

scalaVersion := "2.12.10"

mainClass in Compile := Some("com.example.helloworld.HelloWorldPlayScala")

scalacOptions ++= Seq("-encoding", "UTF-8")

val graalAkkaVersion = "0.5.0"
val GraalVersion = "19.2.1"

libraryDependencies ++= Seq(
  akkaHttpServer,
  // logback,
  filters,

  // GraalVM craziness
  // Only needed for compilation
  "org.graalvm.sdk" % "graal-sdk" % GraalVersion % "provided", 
  "com.oracle.substratevm" % "svm" % GraalVersion % "provided",
  "com.github.vmencik" %% "graal-akka-actor" % graalAkkaVersion,
  "com.github.vmencik" %% "graal-akka-stream" % graalAkkaVersion,
  "com.github.vmencik" %% "graal-akka-http" % graalAkkaVersion,
  "com.github.vmencik" %% "graal-akka-slf4j" % graalAkkaVersion
  )
  
enablePlugins(GraalVMNativeImagePlugin)

graalVMNativeImageOptions ++= Seq(
  "--verbose",
  "-H:+ReportExceptionStackTraces",
  "--no-fallback",
  "-H:+AllowIncompleteClasspath",
  "-H:+TraceClassInitialization",
  "--report-unsupported-elements-at-runtime",
  "--initialize-at-build-time="+ Seq(
    "org.slf4j",
    "ch.qos.logback",
    "scala",
    "akka.dispatch.affinity",
    "akka.util",
    ).mkString(","),

    "--initialize-at-run-time=" + Seq(
      "play.core.server.AkkaHttpServer$$anon$2",
      "com.typesafe.sslconfig.ssl.tracing.TracingSSLContext",
    // We want to delay initialization of these to load the config at runtime
    "com.typesafe.config.impl.ConfigImpl$EnvVariablesHolder",
    "com.typesafe.config.impl.ConfigImpl$SystemPropertiesHolder",
  ).mkString(",")
  )
  

enablePlugins(JavaAppPackaging, DockerPlugin)
// Make sure that the application.conf and possibly other resources are included
import NativePackagerHelper._

mappings in Universal ++= directory( baseDirectory.value / "src" / "main" / "resources" )

// Inherit the package name
packageName in Docker := packageName.value

// Inherit the version
version in Docker := version.value

// You can specify some other jdk Docker image here:
dockerBaseImage := "openjdk"

// If you want to supply specific JVM parameters, do that here:
// javaOptions in Universal ++= Seq()

// If you change this, set this to an email address in the format of: "Name <email adress>"
maintainer := ""

// To use your Docker Hub repository set this to "docker.io/yourusername/yourreponame".
// When using Minikube Docker Repository set it to "dev.local", if you set it to anything else
// then run the following command after `docker:publishLocal`:
//   `docker tag yourreponame/helloworld-scala:<version> dev.local/helloworld-scala:<version>`
dockerRepository := Some("dev.local")

// For more information about which Docker configuration options are available,
// see: https://www.scala-sbt.org/sbt-native-packager/formats/docker.html

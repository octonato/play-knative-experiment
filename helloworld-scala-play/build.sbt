enablePlugins(JavaAppPackaging, DockerPlugin)

organization := "Scala & Play Examples for Knative Serving"

name := "helloworld-scala-play"

version := "0.0.1"

scalaVersion := "2.13.1"

mainClass in Compile := Some("com.example.helloworld.HelloWorldPlayScala")

scalacOptions ++= Seq("-encoding", "UTF-8")

// Make sure that the application.conf and possibly other resources are included
import NativePackagerHelper._

libraryDependencies ++= Seq(
  akkaHttpServer,
  logback,
  filters
)

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

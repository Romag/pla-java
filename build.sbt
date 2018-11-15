name := """play-java-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies ++= Seq(
  javaJpa,
  "org.eclipse.persistence" % "eclipselink" % "2.7.3",
)

/*libraryDependencies ++= Seq(
  ehcache
)*/

/*libraryDependencies ++= Seq(
  cacheApi
)*/

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

//Jquery
libraryDependencies += "org.webjars" % "jquery" % "3.3.1-1"

//Bootstrap
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1"

//Font Awesome
libraryDependencies += "org.webjars" % "font-awesome" % "5.5.0"



PlayKeys.devSettings += "play.server.http.port" -> "9000"

PlayKeys.externalizeResources := false
organization in ThisBuild := "cinnamon.lagom.example"
version in ThisBuild := "0.1-SNAPSHOT"

// The Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

// This sample is not using Kafka
lagomKafkaEnabled in ThisBuild := false

val lombok = "org.projectlombok" % "lombok" % "1.16.18"

lazy val dockerSettings = Seq(
  Docker / maintainer := "Juan Marin Otero",
  dockerBaseImage := "openjdk:jre-alpine",
  dockerRepository := Some("jmarin"),
  dockerExposedPorts ++= Seq(9000, 9001)
)

lazy val helloServiceApi = project
  .in(file("hello-service-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val helloServiceImpl = project
  .in(file("hello-service-impl"))
  .enablePlugins(LagomJava, Cinnamon, SbtReactiveAppPlugin)
  .settings(common: _*)
  .settings(
    // Enable Cinnamon during tests
    cinnamon in test := true,
    // Add a play secret to javaOptions in run in Test, so we can run Lagom forked
    javaOptions in (Test, run) += "-Dplay.http.secret.key=x",
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomLogback,
      lagomJavadslTestKit,
      lombok,
      // Use Coda Hale Metrics and Lagom instrumentation
      Cinnamon.library.cinnamonPrometheus,
      Cinnamon.library.cinnamonPrometheusHttpServer,
      Cinnamon.library.cinnamonLagom
    ),
    dockerSettings
  )
  .dependsOn(helloServiceApi)

lazy val common = Seq(
  javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"),
  // See https://github.com/FasterXML/jackson-module-parameter-names
  javacOptions in compile += "-parameters",
  javaOptions in Universal ++= Seq(
    "-Dpidfile.path=/dev/null"
  )  
)

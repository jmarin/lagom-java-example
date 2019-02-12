organization in ThisBuild := "cinnamon.lagom.example"
version in ThisBuild := "0.1-SNAPSHOT"

// The Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

// This sample is not using Cassandra or Kafka
lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false

lazy val helloServiceApi = project
  .in(file("hello-service-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi
    )
  )

lazy val helloServiceImpl = project
  .in(file("hello-service-impl"))
  .enablePlugins(LagomJava, Cinnamon)
  .settings(common: _*)
  .settings(
    // Enable Cinnamon during tests
    cinnamon in test := true,
    // Add a play secret to javaOptions in run in Test, so we can run Lagom forked
    javaOptions in (Test, run) += "-Dplay.http.secret.key=x",
    libraryDependencies ++= Seq(
      // Use Coda Hale Metrics and Lagom instrumentation
      Cinnamon.library.cinnamonCHMetrics,
      Cinnamon.library.cinnamonLagom
    )
  )
  .dependsOn(helloServiceApi)

lazy val common = Seq(
  javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"),
  // See https://github.com/FasterXML/jackson-module-parameter-names
  javacOptions in compile += "-parameters"
)

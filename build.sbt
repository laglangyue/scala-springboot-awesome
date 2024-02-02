import Dependencies._
import sbt.Keys._
import sbt.{IO, _}

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"
enablePlugins(ScalikejdbcPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "scala-springboot-awesome",
    libraryDependencies += "org.springframework.boot" % "spring-boot" % SpringBoot,
    libraryDependencies += "org.springframework.boot" % "spring-boot-starter-web" % SpringBoot,
    libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.16.1",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % "4.1.0",
      "com.zaxxer" % "HikariCP" % "5.1.0",
      "org.scalikejdbc" %% "scalikejdbc-test" % "4.1.0" % "test",
      "org.scalikejdbc" %% "scalikejdbc-config" % "4.1.0",
      "mysql" % "mysql-connector-java" % "8.0.33",
      // https://mvnrepository.com/artifact/com.h2database/h2
      "com.h2database" % "h2" % "2.2.224",
      "org.scalatest" %% "scalatest" % "3.2.17" % Test
    )
  )

(Compile / scalikejdbcJDBCSettings) := {
  val props = new java.util.Properties()
  IO.load(props, file("project/scalikejdbc.properties"))
  def loadProp(key: String): String =
    Option(props.get(key))
      .map(_.toString)
      .getOrElse(throw new IllegalStateException("missing key " + key))

  val settings = JDBCSettings(
    driver = loadProp("jdbc.driver"),
    url = loadProp("jdbc.url"),
    username = loadProp("jdbc.username"),
    password = loadProp("jdbc.password"),
    schema = loadProp("jdbc.schema")
  )
  println(settings)
  settings
}

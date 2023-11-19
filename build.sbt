ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "KotyaProject"
  )


libraryDependencies += "dev.zio" %% "zio-json" % "0.6.2"

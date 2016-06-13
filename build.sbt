name := "aplicacionMigrana"

version := "1.0"

lazy val `aplicacionmigrana` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies ++= Seq(
  cache,
  ws,
  specs2 % Test,
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0"
)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"


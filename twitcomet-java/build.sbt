name := "twitcomet-java"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  javaEbean,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-api" % "3.1.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.3"  
)

play.Project.playJavaSettings

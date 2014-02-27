name := "ODataUriParser"

version := "0.1"

scalaVersion := "2.10.3"

val MavenRepo    =   "Maven repository" at "http://repo1.maven.org/maven2/"
val JBossRepo    =   "JBoss repository" at "https://repository.jboss.org/nexus/content/groups/public/"

resolvers ++= Seq(MavenRepo, JBossRepo)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final" % "provided",
  "org.hibernate" % "hibernate-entitymanager" % "4.2.8.Final",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "runtime",
  "com.google.guava" % "guava" % "16.0.1"
)

mainClass := Some("Main")
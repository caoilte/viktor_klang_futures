name := "Futures"
 
version := "1.0"
 
scalaVersion := "2.9.2"
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0.1" withSources,
  "io.netty" % "netty" % "3.3.0.Final" withSources,
  "com.ning" % "async-http-client" % "1.7.4" withSources,
  "org.scalatest" % "scalatest_2.9.1" % "1.6.1" % "test")

import pl.project13.scala.sbt.JmhPlugin

organization := "me.maciejb"
name := "roaring-buffer-perf"
version := "1.0"
scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.roaringbitmap" % "RoaringBitmap" % "0.6.20",
  "org.roaringbitmap" % "real-roaring-dataset" % "0.1-SNAPSHOT"
)

lazy val roaringBufferPerf = project
  .in(file("."))
  .enablePlugins(JmhPlugin)

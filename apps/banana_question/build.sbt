packSettings

packMain := Map("banana_question" -> "banana_question")

name := "BananaQuestion"

version := "0.3"

organization := "edu.umass.cs"

scalaVersion := "2.11.7"

exportJars := true

libraryDependencies ++= Seq(
  "edu.umass.cs" %% "automan" % "1.2-SNAPSHOT",
  "com.amazonaws"   %  "aws-java-sdk"	% "1.10.23",
  "org.imgscalr"    %  "imgscalr-lib"	% "4.2"
)

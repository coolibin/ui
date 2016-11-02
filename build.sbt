val gVersion = "2.1.7"

name := "test-fw"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies +=  "io.gatling.highcharts" % "gatling-charts-highcharts" % gVersion
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % gVersion
libraryDependencies +=  "joda-time"             % "joda-time"                 % "2.8"
libraryDependencies +=  "org.joda" % "joda-convert" % "1.8"
libraryDependencies +=  "org.seleniumhq.selenium" % "selenium-java" % "2.53.0"
  //      "org.testng" % "testng" % "5.7" classifier "jdk15"
libraryDependencies +=  "org.testng" % "testng" % "6.9.10"
libraryDependencies +=  "org.hamcrest" % "hamcrest-all" % "1.3"
libraryDependencies +=  "net.liftweb" %% "lift-json" % "2.6+"
libraryDependencies +=  "com.gingersoftware" % "object-csv_2.11" % "0.2"
libraryDependencies +=  "org.scalatest" % "scalatest_2.11" % "2.2.4"
  //      "org.scalatest" % "scalatest" % "2.0.M6" % "test->*",
libraryDependencies +=  "com.codepine.api" % "testrail-api-java-client" % "1.0.0"
  //      "org.apache.logging.log4j" %  "log4j-api"    % "2.5",
  //      "org.apache.logging.log4j" %  "log4j-core"   % "2.5",
libraryDependencies +=  "com.google.code.findbugs" % "jsr305" % "3.0.0"
libraryDependencies +=  "com.googlecode.json-simple" % "json-simple" % "1.1"
libraryDependencies +=  "org.json4s" % "json4s-native_2.11" % "3.3.0"
libraryDependencies +=  "mysql" % "mysql-connector-java" % "5.1.38"
libraryDependencies +=  "com.typesafe.slick" % "slick_2.11" % "2.1.0"
libraryDependencies +=  "com.google.inject" % "guice" % "3+"
libraryDependencies +=  "com.jcraft" % "jsch" % "0.1.53"


parallelExecution in Test := true

// to suppress unnecessary ScalaTest messages
showSuccess := false
logLevel in Test := Level.Error

//redirect feed folder when executed as sbt
javaOptions := Seq("-Dnateraqa.feed.feedFolder=user-files")

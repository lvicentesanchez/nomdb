import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
)

// Dependencies
val compilerPlugins = Seq(
)

val rootDependencies = Seq(
  "ch.qos.logback"      %  "logback-classic" % "1.2.3",
  "ch.qos.logback"      %  "logback-core"    % "1.2.3",
  "com.github.finagle"  %% "finch-core"      % "0.14.0",
  "com.github.finagle"  %% "finch-circe"     % "0.14.0",
  "com.h2database"      %  "h2"              % "1.4.194",
  "com.twitter"         %% "twitter-server"  % "1.28.0",
  "com.typesafe.slick"  %% "slick"           % "3.2.0",
  "com.typesafe.slick"  %% "slick-hikaricp"  % "3.2.0",
  "io.circe"            %% "circe-generic"   % "0.7.1",
  "io.circe"            %% "circe-parser"    % "0.7.1",
  "org.flywaydb"        %  "flyway-core"     % "4.1.2",
  "org.sangria-graphql" %% "sangria"         % "1.1.0",
  "org.sangria-graphql" %% "sangria-circe"   % "1.0.1"
)

val testDependencies = Seq (
)

val dependencies =
  compilerPlugins ++
  rootDependencies ++
  testDependencies

// Settings
//
val compileSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint",
  //"-Ybackend:GenBCode",
  "-Ydelambdafy:method",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=GMT",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops"
)

val pluginsSettings =
  scalariformSettings

val settings = Seq(
  name := "nomdb",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.1",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions := compileSettings,
  // formatting
  //
  ScalariformKeys.preferences := PreferencesImporterExporter.loadPreferences(( file(".") / "formatter.preferences").getPath)
)

val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )

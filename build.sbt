import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
)

// Dependencies
val compilerPlugins = Seq(
)

val rootDependencies = Seq(
  "com.github.finagle"  %% "finch-core"     % "0.11.0-LOCAL",
  "com.github.finagle"  %% "finch-circe"    % "0.11.0-LOCAL",
  "com.twitter"         %% "twitter-server" % "1.20.0",
  "io.circe"            %% "circe-generic"  % "0.5.1",
  "io.circe"            %% "circe-parser"   % "0.5.1",
  "org.sangria-graphql" %% "sangria"        % "0.7.3",
  "org.sangria-graphql" %% "sangria-circe"  % "0.5.0"
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
  "-Ybackend:GenBCode",
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
  scalaVersion := "2.11.8",
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

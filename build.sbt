// Resolvers
resolvers ++= Seq(
)

// Dependencies
val compilerPlugins = Seq(
)

val rootDependencies = Seq(
  "ch.qos.logback"      %  "logback-classic" % "1.2.3",
  "ch.qos.logback"      %  "logback-core"    % "1.2.3",
  "com.github.finagle"  %% "finch-core"      % "0.15.1",
  "com.github.finagle"  %% "finch-circe"     % "0.15.1",
  "com.h2database"      %  "h2"              % "1.4.196",
  "com.twitter"         %% "twitter-server"  % "1.30.0",
  "com.typesafe.slick"  %% "slick"           % "3.2.1",
  "com.typesafe.slick"  %% "slick-hikaricp"  % "3.2.1",
  "io.circe"            %% "circe-generic"   % "0.8.0",
  "io.circe"            %% "circe-parser"    % "0.8.0",
  "org.flywaydb"        %  "flyway-core"     % "4.2.0",
  "org.sangria-graphql" %% "sangria"         % "1.2.2",
  "org.sangria-graphql" %% "sangria-circe"   % "1.1.0"
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

val pluginsSettings = Seq()

val settings = Seq(
  name := "nomdb",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.3",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions := compileSettings
)

val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )

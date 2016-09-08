// Comment to get more information during initialization
//
logLevel := Level.Warn

// Resolvers
//

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
      
resolvers += "Softprops Maven" at "http://dl.bintray.com/content/softprops/maven"

// Build Info
//
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")

// Native Packager
//
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.1")

// Releases
//
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

// Scalariform
//
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
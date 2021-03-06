lazy val baseName   = "ToyIDE"
lazy val baseNameL  = baseName.toLowerCase

lazy val commonSettings = Seq(
  version            := "1.3.0-SNAPSHOT",
  organization       := "com.pavelfatin",
  homepage           := Some(url("https://pavelfatin.com/toyide")),
  licenses           := Seq("Apache License v2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  scalaVersion       := "2.12.8",
  crossScalaVersions := Seq("2.12.8", "2.11.12"),
  scalacOptions     ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xfuture"),
  fork in Test       := false
)

lazy val testSettings = Seq(
  libraryDependencies ++= Seq(
    "junit"        % "junit"           % "4.12" % Test,
    "com.novocode" % "junit-interface" % "0.11" % Test
  ),
)

lazy val root = project.in(file("."))
  .aggregate(core, lisp, toy, scalalang, ui, app)
  .settings(
    name := baseName
  )

lazy val core = project.in(file("core"))
  .settings(commonSettings)
  .settings(testSettings)
  .settings(
    name := baseName
//    libraryDependencies ++= Seq(
//      "net.sourceforge.jasmin" % "jasmin" % "1.1",
//    ),
  )

lazy val lisp = project.in(file("lisp"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(commonSettings)
  .settings(testSettings)
  .settings(
    name := s"$baseName - Clojure-like functional language",
    unmanagedResourceDirectories in Compile += baseDirectory.value / "src" / "main" / "lisp"
  )

lazy val toy = project.in(file("toy"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(commonSettings)
  .settings(testSettings)
  .settings(
    name := s"$baseName - C-like imperative language"
  )

lazy val scalalang = project.in(file("scalalang"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(commonSettings)
  .settings(testSettings)
  .settings(
    name := s"$baseName - Scala language",
    libraryDependencies ++= Seq(
      "org.scala-lang"  %  "scala-compiler" % scalaVersion.value,
      "org.scalameta"   %% "scalameta"      % "4.1.0",
      "org.scalariform" %% "scalariform"    % "0.2.6"
    )
  )

lazy val ui = project.in(file("ui"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(commonSettings)
  .settings(testSettings)
  .settings(
    name := s"$baseName - graphical user interface",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-swing" % "2.0.3"
    )
  )

lazy val app = project.in(file("app"))
  .dependsOn(ui, lisp, toy, scalalang)
  .settings(commonSettings)
  .settings(
    name := s"$baseName - demo application",
    mainClass in Compile := Some("com.pavelfatin.toyide.Application")
  )

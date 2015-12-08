enablePlugins(AllHaxePlugins)

organization := "com.thoughtworks.microbuilder"

name := "haxe-import-csv"

autoScalaLibrary := false

crossPaths := false

for {
  c <- AllTargetConfigurations ++ AllTestTargetConfigurations
} yield {
  haxeOptions in c ++=
    Seq("-D", "using_worksheet")
}

val haxelibs = Map(
  "continuation" -> DependencyVersion.SpecificVersion("1.3.2"),
  "haxeparser" -> DependencyVersion.SpecificVersion("1.0.0"),
  "hxparse" -> DependencyVersion.SpecificVersion("4.0.0")
)

haxelibDependencies ++= haxelibs

for (c <- AllTargetConfigurations ++ AllTestTargetConfigurations) yield {
  haxeOptions in c ++= haxelibOptions(haxelibs)
}

for (c <- Seq(CSharp, TestCSharp)) yield {
  haxeOptions in c ++= Seq("-D", "dll")
}

haxeOptions in Test ++= Seq("--macro", "importCsv.Importer.importCsv(['importCsv/TestConfig.xlsx.Foo.utf-8.csv','importCsv/TestConfig.xlsx.Sheet2.utf-8.csv','importCsv/TestConfig.xlsx.Sheet3.utf-8.csv','importCsv/TestConfig.xlsx.import.utf-8.csv','importCsv/TestConfig.xlsx.using.utf-8.csv'])")

haxeOptions in Test ++= Seq("-main", "importCsv.ImporterTest")

sourceGenerators in TestHaxe <+= Def.task {
  val xlsxBase = (sourceDirectory in TestHaxe).value
  val unzippedBase = (sourceManaged in TestHaxe).value
  val unzipXlsx = FileFunction.cached(
    streams.value.cacheDirectory / ("unzip_xlsx_" + scalaVersion.value),
    inStyle = FilesInfo.lastModified,
    outStyle = FilesInfo.exists) { xlsxFiles: Set[File] =>
      streams.value.log.info(s"Unzipping ${xlsxFiles.size} XLSX files...")
      for {
        xlsxFile <- xlsxFiles
        if !xlsxFile.isHidden // 跳过备份文件
        outputFile <- IO.unzip(xlsxFile, unzippedBase / xlsxFile.relativeTo(xlsxBase).get.toString)
      } yield outputFile
    }
  unzipXlsx((xlsxBase ** "*.xlsx").get.toSet).toSeq
}

crossScalaVersions := Seq("2.11.2")

homepage := Some(url(s"https://github.com/ThoughtWorksInc/${name.value}"))

startYear := Some(2014)

licenses := Seq("Apache" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

releaseUseGlobalVersion := false

releasePublishArtifactsAction := PgpKeys.publishSigned.value

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepTask(publish in Haxe),
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeRelease"),
  pushChanges
)

scmInfo := Some(ScmInfo(
  url(s"https://github.com/ThoughtWorksInc/${name.value}"),
  s"scm:git:git://github.com/ThoughtWorksInc/${name.value}.git",
  Some(s"scm:git:git@github.com:ThoughtWorksInc/${name.value}.git")))

haxelibContributors := Seq("Atry")

developers := List(
  Developer(
    "Atry",
    "杨博 (Yang Bo)",
    "pop.atry@gmail.com",
    url("https://github.com/Atry")
  ),
  Developer(
    "zxiy",
    "张修羽 (Zhang Xiuyu)",
    "95850845@qq.com",
    url("https://github.com/zxiy")
  )
)

haxelibReleaseNote := "First release to haxelib."

haxelibTags ++= Seq(
  "cross", "cpp", "cs", "flash", "java", "javascript", "js", "neko", "php", "python", "nme",
  "csv", "data", "macro", "serialization", "configuration"
)

// vim: sts=2 sw=2 et

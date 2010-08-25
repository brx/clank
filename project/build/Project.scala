import sbt._
import java.io.File

class Project(info: ProjectInfo) extends DefaultProject(info) with ProguardProject {
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.0" % "1.7"

  override val proguardOptions =
    List("-keep class com.clank.Clank { public static void main(java.lang.String[]); }",
         "-dontnote")
}

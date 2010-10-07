import sbt._
import java.io.File

class Project(info: ProjectInfo) extends DefaultProject(info) with ProguardProject {
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.0" % "1.7"

  override def proguardInJars = super.proguardInJars +++ buildLibraryJar

  override val proguardDefaultArgs =
    List(
      "-dontwarn",
      "-dontnote",
      "-keepattributes SourceFile,LineNumberTable",
      "-keep class com.clank.Clank { public static void main(java.lang.String[]); }",
    )
}

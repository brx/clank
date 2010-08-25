package com.clank

import scala.util.matching.Regex

import java.io.PrintStream
import java.io.FileOutputStream

import spellers.Speller

object Clanker {

  private def printAmb(lineNo: Int, columnNo: Int, outFile: Option[String]) {
    val filePart = outFile match {
      case Some(name) => "[" + name + "]"
      case None       => ""
    }
    val posPart = "(" + lineNo + "," + columnNo + ")"

    Console.err.println("| Ambiguity" + filePart + posPart + " |")
  }

  private def withClankerOut(outFile: Option[String])(block: => Unit) {
    val out = outFile match {
      case Some(name)   => new java.io.FileOutputStream(name)
      case None         => Console.out
    }

    try
      Console.withOut(out)(block)
    finally
      if (out.isInstanceOf[FileOutputStream]) out.close()
  }

  def apply(input: Iterator[String], outFile: Option[String], speller: Speller,
            rxWord: Regex, disregard: String => Boolean) {
    withClankerOut(outFile) {
      for ((line, lineIdx) <- input.zipWithIndex) {
        val lineNo = lineIdx + 1

        var pos = 0
        var columnOffset = 0

        for (m <- rxWord.findAllIn(line).matchData) {
          val columnNo = 1 + m.start + columnOffset

          val word = m.toString
          val spellings = if (disregard(word)) Nil else speller.spell(word)

          print(line.substring(pos, m.start))

          spellings match {
            case Nil          => print(word)
            case List(head)   => print(head)
            case _            => {
              printAmb(lineNo, columnNo, outFile)
              print(spellings.mkString("<", "|", ">"))

              columnOffset += (spellings.length - 1) * word.length + 3
            }
          }

          pos = m.end
        }

        println(line.substring(pos))
      }
    }
  }
}

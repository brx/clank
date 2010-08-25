package com.clank.dicts

import scala.io.Source

class SetDictionary(private val wordSet: Set[String]) extends Dictionary {
  def contains(word: String) = wordSet contains word

  override def toString = "SetDictionary(" + wordSet.mkString(", ") + ")"
}

object SetDictionary {
  def fromFile(file: String): SetDictionary =
    new SetDictionary((Source fromFile file).getLines.toSet)

  implicit def setDict2Set(sd: SetDictionary): Set[String] = sd.wordSet
}

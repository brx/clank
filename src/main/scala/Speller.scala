package com.clank.spellers

abstract class Speller {
  def spell(word: String): List[String]
}

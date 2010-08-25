package com.clank.spellers

import com.clank.dicts.Dictionary

trait DictionaryCheck extends Speller {
  def dict: Dictionary

  abstract override def spell(word: String): List[String] =
    super.spell(word) filter dict.contains

  override def toString = super.toString + "\n| with " + dict
}

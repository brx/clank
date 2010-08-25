package com.clank.spellers

trait KeepCapitals extends Speller {
  abstract override def spell(word: String): List[String] = {
    val isCapital = word.head.isUpper
    super.spell(word) map (w => if (isCapital) w.capitalize else w)
  }
}

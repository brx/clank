package com.clank.spellers

import com.clank.util.CharSugar._

trait CapitalBridge extends CharacterTranslator {
  abstract override def spell(word: String): List[String] = {
    val tHead = word(0).toggleCase
    val heads = charMap(tHead) + tHead
    val results = super.spell(word)

    results ++ (results flatMap (res => heads map (_ + res.tail))) distinct
  }
}

package com.clank.util

object CharSugar {
  class SugarChar(ch: Char) {
    def toggleCase = if (ch.isLower) ch.toUpper else ch.toLower
  }

  implicit def ch2sCh(ch: Char) = new SugarChar(ch)
}

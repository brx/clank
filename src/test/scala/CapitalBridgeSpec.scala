package com.clank.tests

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

import org.scalacheck._
import Prop._

import com.clank.spellers.CapitalBridge
import com.clank.spellers.CharacterTranslator

import com.clank.util.CharSugar._

class CapitalBridgeSpec extends WordSpec with Checkers {
  import Helper._

  "A CharacterTranslator with CapitalBridge" should {

    def generateResults(word: NiceString, trs: Map[NiceChar, Set[NiceChar]]) =
      (new CharacterTranslator(trs) with CapitalBridge).spell(word)

    "generate the right number of possible spellings" in {
      check((trs: NiceMap[NiceChar, NiceSet[NiceChar]], word: NiceString) => {
        val toggled = word(0).toggleCase
        val origCaseSpells = trs.getOrElse(word(0), Set()) + word(0)
        val toggledCaseSpells = trs.getOrElse(toggled, Set()) + toggled
        val results = generateResults(word, trs)

        word.tail.foldLeft(1)((n, ch) => n * (trs.getOrElse(ch, Set()) + ch).size) *
        (origCaseSpells ++ toggledCaseSpells).size == results.length
      })
    }

    "touch only characters to be mapped" in {
      def involatiles(word: String, volatile: Set[NiceChar]) =
        word.zipWithIndex filterNot (volatile contains _._1) map (_._2) toSet

      check((trs: NiceMap[NiceChar, NiceSet[NiceChar]], word: NiceString) => {
        val involatileIndices  = involatiles(word, trs.keys.toSet) - 0
        val involatileInput = involatileIndices map word

        generateResults(word, trs) forall {
          result => (involatileIndices map result) == involatileInput
        }
      })
    }

    "not generate duplicate spellings" in {
      check((trs: NiceMap[NiceChar, NiceSet[NiceChar]], word: NiceString) => {
        val results = generateResults(word, trs)

        results.length == results.distinct.length
      })
    }
  }
}

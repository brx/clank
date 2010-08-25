package com.clank.tests

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

import org.scalacheck._

import com.clank.spellers.CharacterTranslator

class CharacterTranslatorSpec extends WordSpec with Checkers {
  import Helper._

  "A CharacterTranslator" should {

    def generateResults(word: NiceString, trs: Map[NiceChar, Set[NiceChar]]) =
      (new CharacterTranslator(trs)).spell(word)

    "generate the right number of possible spellings" in {
      check((trs: NiceMap[NiceChar, NiceSet[NiceChar]], word: NiceString) =>
        word.foldLeft(1)((n, ch) => n * (trs.getOrElse(ch, Set()) + ch).size) ==
          generateResults(word, trs).length)
    }

    "touch only characters to be mapped" in {
      def involatiles(word: String, volatile: Set[NiceChar]) =
        word.zipWithIndex filterNot (volatile contains _._1) map (_._2)

      check((trs: NiceMap[NiceChar, NiceSet[NiceChar]], word: NiceString) => {
        val involatileIndices: Seq[Int] = involatiles(word, trs.keys.toSet)
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

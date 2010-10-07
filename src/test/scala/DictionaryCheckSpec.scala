package com.clank.tests

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

import org.scalacheck._
import Arbitrary.arbitrary
import Gen._

import com.clank.dicts._

import com.clank.spellers.DictionaryCheck

class DictionaryCheckSpec extends WordSpec with Checkers {
  import Helper._

  "A DumbSpeller with DictionaryCheck" should {

     class DictCheckSpeller(words: List[String], dt: Dictionary)
     extends DumbSpeller(words) with DictionaryCheck { val dict = dt }

    implicit val arbDictCheckSpeller = Arbitrary(
      for {
        dictSet <- arbitrary[Set[NiceString]]
        words <- listOf(oneOf(arbitrary[NiceString], oneOf(dictSet.toSeq)))
      } yield new DictCheckSpeller(words.distinct, new SetDictionary(dictSet))
    )

    "only return things contained in Dictionary" in {
      check((speller: DictCheckSpeller, word: NiceString) =>
        speller.spell(word) forall speller.dict.contains)
    }
  }
}

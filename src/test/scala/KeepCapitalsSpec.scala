package com.clank.tests

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

import org.scalacheck._
import Arbitrary.arbitrary
import Prop._

import com.clank.spellers.KeepCapitals

class KeepCapitalsSpec extends WordSpec with Checkers {
  import Helper._

  "A DumbSpeller with KeepCapitals" should {

    implicit val arbDumbStringWithKeepCapitals =
      mkSimpleDumbSpellerArb(new DumbSpeller(_) with KeepCapitals)
    
    def normNiceStrings(niceStrings: List[NiceString]) =
      niceStrings map (_.capitalize) distinct

    "touch only the first letter of a result word" in {
      check((speller: DumbSpeller, word: NiceString) =>
        normNiceStrings(speller.words) zip
        normNiceStrings(speller.spell(word)) forall {
          case (a, b) => a.tail == b.tail
        })
    }

    "preserve capital state of capitalized input words" in {
      check((speller: DumbSpeller, word: NiceString) => word.head.isUpper ==> (
        normNiceStrings(speller.words) zip speller.spell(word) forall {
          case (a, b) => b.head.isUpper && b.head == a.capitalize.head
        }
      ))
    }

    "keep output capital state for uncapitalized input" in {
      check((speller: DumbSpeller, word: NiceString) => word.head.isLower ==> (
        speller.words zip speller.spell(word) forall {
          case (a, b) => a.head == b.head
        }
      ))
    }

    "not generate duplicate spellings" in {
      check((speller: DumbSpeller, word: NiceString) => {
        val spellings = speller.spell(word)
        spellings.length == spellings.distinct.length
      })
    }
  }
}

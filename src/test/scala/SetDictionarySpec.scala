package com.clank.tests

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

import org.scalacheck._
import Arbitrary.arbitrary
import Prop._
import Gen._

import com.clank.dicts.SetDictionary

class SetDictionarySpec extends WordSpec with Checkers {

  "A SetDictionary" when {

    "set up with a valid dictionary" should {

      val dictPositiveSample = for {
        dictSet <- arbitrary[Set[String]]
        sample <- someOf(dictSet)
      } yield (new SetDictionary(dictSet), sample)

      val dictNegativeSample = for {
        dictSet <- arbitrary[Set[String]]
        sample <- arbitrary[Set[String]]
      } yield (new SetDictionary(dictSet), sample diff dictSet)

      "contain some words" in {
        check(forAll(dictPositiveSample) {
          case (dict, sample) => sample forall dict.contains
        })
      }

      "NOT contain some words" in {
        check(forAll(dictNegativeSample) {
          case (dict, sample) => sample forall (!dict.contains(_))
        })
      }
    }

    "set up with a non-existing dictionary file" should {

      "throw a java.io.FileNotFoundException" in {
        intercept[java.io.FileNotFoundException] {
          SetDictionary.fromFile("I hope this does not exist?!")
        }
      }
    }
  }
}

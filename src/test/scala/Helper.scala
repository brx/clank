package com.clank.tests

import org.scalacheck._
import Gen._
import Arbitrary.arbitrary

import com.clank.spellers._

object Helper {
  class DumbSpeller(val words: List[String]) extends Speller {
    def spell(word: String): List[String] = words
    override def toString = "DumbSpeller(" + words.mkString(", ") + ")"
  }

  type NiceChar = Char
  type NiceString = String
  type NiceSet[T] = Set[T]
  type NiceMap[K, V] = Map[K, V]

  implicit val arbNiceChar: Arbitrary[NiceChar] =
    Arbitrary(oneOf(alphaUpperChar, alphaLowerChar))

  implicit val arbNiceString: Arbitrary[NiceString] =
    Arbitrary((for {
      h <- arbitrary[NiceChar]
      n <- choose(2, 20)
      t <- listOfN(n, alphaLowerChar)
    } yield (h :: t).mkString) suchThat (!_.isEmpty))

  implicit def arbNiceSet[T](implicit ae: Arbitrary[T]): Arbitrary[NiceSet[T]] =
    Arbitrary(for {
      n <- choose(1, 3)
      l <- containerOfN[Set, T](n, ae.arbitrary)
    } yield l.toSet)

  implicit def arbNiceMap[K, V](implicit ak: Arbitrary[K],
                                av: Arbitrary[V]): Arbitrary[NiceMap[K, V]] =
    Arbitrary(for {
      n <- choose(1, 6)
      keys <- listOfN(n, ak.arbitrary)
      vals <- listOfN(n, av.arbitrary)
    } yield Map(keys zip vals: _*))

  def mkSimpleDumbSpellerArb(constructor: List[NiceString] => DumbSpeller) =
    Arbitrary(listOf(Arbitrary.arbitrary[NiceString]) map constructor)
}

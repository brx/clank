package com.clank.spellers

class CharacterTranslator(chM: Map[Char, Set[Char]]) extends Speller {
  protected val charMap = chM.withDefaultValue(Set())

  def spell(word: String): List[String] = {
    val builder = new StringBuilder(word)
    val mutants = word.zipWithIndex filter (charMap contains _._1)

    def buildSpellings(mutants: Seq[(Char, Int)]): Set[String] =
      if (mutants.isEmpty) Set(builder.toString)
      else {
        val (ch, idx) = mutants.head
        (charMap(ch) + ch) flatMap {
          spelling => {
            builder.update(idx, spelling)
            buildSpellings(mutants.tail)
          }
        }
      }

    buildSpellings(mutants).toList
  }
}

package com.clank

import scala.io.Source
import scala.util.matching.Regex

class Config(opts: String*) {
  private val options = opts.toSet

  def parseConfig(src: Source): Map[String, String] = {
    val parsedOpts = (for {
      line <- src.getLines
      sides = line.split("=", 2)
      lhs = sides(0).trim
      if options contains lhs
      rhs = sides(1).trim
    } yield lhs -> rhs).toMap

    for (option <- options)
      if (!parsedOpts.contains(option))
        throw new Config.MissingOption(option)

    parsedOpts
  }
}

object Config {
  class MissingOption(val option: String) extends Exception

  def optTr(str: String): Map[Char, Set[Char]] = {
    val trs = for {
      tr <- str.split(':')
      trimmed = tr.trim
      from = trimmed.charAt(0)
      to = trimmed.substring(1).toSet
    } yield from -> to

    trs.toMap
  }
}

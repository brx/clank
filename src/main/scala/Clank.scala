package com.clank

import scala.io.Source

import dicts._
import spellers._

import Config._

object Clank {
  def main(args: Array[String]) {
    val configPath = System.getProperty("user.home") + "/.clankrc"
    val config = new Config("dict", "wordrx", "translations", "maxwordlength")

    try {
      val options = config.parseConfig(Source.fromFile(configPath))

      val rxWord = options("wordrx").r
      val speller = new CharacterTranslator(optTr(options("translations")))
      with CapitalBridge with DictionaryCheck with KeepCapitals {
        val dict = SetDictionary.fromFile(options("dict"))
      }

      val outFile = if (args.isEmpty) None else Some(args(0))
      val maxWordLength = options("maxwordlength").toInt

      Clanker(Source.stdin.getLines(), outFile, speller, rxWord,
              (_.length > maxWordLength))
    } catch {
      case ex: MissingOptionException =>
        Console.err.println(
          "Missing option '" + ex.option +"' in " + configPath + "!"
        )
        System.exit(1)
    }
  }
}

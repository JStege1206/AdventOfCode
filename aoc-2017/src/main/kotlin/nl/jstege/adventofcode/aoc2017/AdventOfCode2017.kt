package nl.jstege.adventofcode.aoc2017

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.mainBody
import nl.jstege.adventofcode.aoccommon.AdventOfCode
import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class AdventOfCode2017(parser: ArgParser) :
        AdventOfCode(parser, AdventOfCode2017::class.java.`package`.name + ".days")

/**
 * Starts the application. Without an argument, this application will run
 * all [Day]s. When passed at least one integer between 1 and 25,
 * the application will only run those days.
 *
 * @param args The arguments to run the program with. Please use --help to get all possible options
 * or look at the documentation for [AdventOfCode].
 */
fun main(args: Array<String>) = mainBody("Advent Of Code 2017") {
    AdventOfCode.run(AdventOfCode2017(ArgParser(args, helpFormatter = DefaultHelpFormatter())))
}
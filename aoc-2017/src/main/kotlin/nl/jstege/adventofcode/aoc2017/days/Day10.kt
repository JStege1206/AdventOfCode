package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoc2017.utils.hash.knot.KnotHash.knotHash
import nl.jstege.adventofcode.aoc2017.utils.hash.knot.KnotHash.scramble
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day(title = "Knot Hash") {
    private companion object Configuration {
        private const val FIRST_ELEMENTS_TO_MULTIPLY = 2
    }

    override fun first(input: Sequence<String>): Any {
        return input.head
            .split(",")
            .map(String::toInt)
            .scramble()
            .take(FIRST_ELEMENTS_TO_MULTIPLY)
            .reduce(Int::times)
    }

    override fun second(input: Sequence<String>): Any {
        return input.head.knotHash().toHexString()
    }
}

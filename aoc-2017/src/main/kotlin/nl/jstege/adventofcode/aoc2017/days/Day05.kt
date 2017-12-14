package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day() {
    override suspend fun first(input: Sequence<String>): Any {
        return input.parse().walk { 1 }
    }

    override suspend fun second(input: Sequence<String>): Any {
        return input.parse().walk { if (it >= 3) -1 else 1 }
    }

    private fun Sequence<String>.parse(): IntArray = this
            .map(String::toInt)
            .toList()
            .toIntArray()

    private fun IntArray.walk(incrementalStep: (Int) -> Int): Int {
        tailrec fun walk(i: Int, accumulator: Int = 0): Int {
            if (i >= this.size || i < 0) return accumulator
            val t = this[i]
            this[i] += incrementalStep(t)
            return walk(i + t, accumulator + 1)
        }
        return walk(0)
    }
}

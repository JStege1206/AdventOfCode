package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day(title = "A Maze of Twisty Trampolines, All Alike") {
    override fun first(input: Sequence<String>): Any =
        input.parse().walk { 1 }

    override fun second(input: Sequence<String>): Any =
        input.parse().walk { if (it >= 3) -1 else 1 }

    private fun Sequence<String>.parse(): IntArray = this
        .map(String::toInt)
        .toList()
        .toIntArray()

    private fun IntArray.walk(incrementalStep: (Int) -> Int): Int {
        tailrec fun walkRecursive(i: Int, accumulator: Int = 0): Int =
            if (i >= this.size || i < 0) accumulator
            else walkRecursive(
                i + this[i].apply { this@walk[i] += incrementalStep(this) }, accumulator + 1
            )

        return walkRecursive(0)
    }
}

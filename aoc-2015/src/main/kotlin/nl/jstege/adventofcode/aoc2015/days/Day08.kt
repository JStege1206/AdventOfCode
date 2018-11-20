package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day(title = "Matchsticks") {

    override fun first(input: Sequence<String>): Any = input
        .sumBy { it.length - it.strippedLength }

    override fun second(input: Sequence<String>): Any = input
        .sumBy { it.escapedLength - it.length }

    private val String.strippedLength: Int
        get() {
            tailrec fun calculateStrippedLength(index: Int, acc: Int): Int =
                if (index >= this.length - 1) acc
                else when (this[index]) {
                    '\\' -> when (this[index + 1]) {
                        'x' -> calculateStrippedLength(index + 4, acc + 1)
                        else -> calculateStrippedLength(index + 2, acc + 1)
                    }
                    else -> calculateStrippedLength(index + 1, acc + 1)
                }
            return calculateStrippedLength(1, 0)
        }

    private val String.escapedLength: Int
        get() = this.sumBy { if (it == '"' || it == '\\') 2 else 1 } + 2
}

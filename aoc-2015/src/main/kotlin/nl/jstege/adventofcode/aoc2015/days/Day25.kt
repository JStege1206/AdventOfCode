package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day25 : Day(title = "Let It Snow") {
    private companion object Configuration {
        private const val STARTING_VALUE = 20151125L
        private const val MULTIPLY_VALUE = 252533L
        private const val DIVIDING_VALUE = 33554393L
        private const val MULDIV_VALUE = MULTIPLY_VALUE % DIVIDING_VALUE
    }

    override fun first(input: Sequence<String>): Any {
        val (row, column) = input.parse()
        var currentRow = 1
        var currentColumn = 1
        var maxRow = 2
        var value = STARTING_VALUE

        while (currentRow != row || currentColumn != column) {
            currentRow--
            currentColumn++
            if (currentRow < 1) {
                currentRow = maxRow
                currentColumn = 1
                maxRow++
            }
            value = value % DIVIDING_VALUE * MULDIV_VALUE % DIVIDING_VALUE
        }

        return value
    }

    override fun second(input: Sequence<String>): Any {
        return "-"
    }


    private fun Sequence<String>.parse(): Pair<Int, Int> = this.head
        .replace("[^0-9]".toRegex(), " ")
        .replace("\\s+".toRegex(), " ")
        .trim().split(" ")
        .let { (row, column) -> row.toInt() to column.toInt() }

}

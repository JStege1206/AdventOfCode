package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day() {
    override suspend fun first(input: Sequence<String>): Any = input
            .sumBy { it.length - it.unescapedLength }

    override suspend fun second(input: Sequence<String>): Any = input
            .sumBy { it.escapedLength - it.length }

    private val String.unescapedLength: Int
        get() {
            var length = 0

            var i = 1
            while (i < this.length - 1) {
                if (this[i] == '\\') {
                    i += (if (this[i + 1] == 'x') 3 else 1)
                }

                length++
                i++
            }

            return length
        }

    private val String.escapedLength: Int
        get() = this.fold(2) { length, c ->
            length + (if (c == '"' || c == '\\') 2 else 1)
        }
}

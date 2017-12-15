package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    override fun first(input: Sequence<String>): Any = input.parse { it }

    override fun second(input: Sequence<String>): Any = input.parse { it.toList().sorted() }

    private inline fun <E> Sequence<String>.parse(mapFunction: (String) -> E): Int =
            this.map { it.split(' ') }.count { it.size == it.map(mapFunction).toSet().size }
}

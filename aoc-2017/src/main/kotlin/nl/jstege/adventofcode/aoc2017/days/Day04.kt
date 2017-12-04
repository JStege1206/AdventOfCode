package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    override fun first(input: Sequence<String>): Any {
        return input.parse { this }
    }

    override fun second(input: Sequence<String>): Any {
        return input.parse { this.map { it.toSet() } }
    }

    private inline fun <E> Sequence<String>.parse(mapper: (List<String>.() -> List<E>)): Int =
            this
                    .map { it.split(' ') }
                    .count { it.size == it.mapper().toSet().size }
}

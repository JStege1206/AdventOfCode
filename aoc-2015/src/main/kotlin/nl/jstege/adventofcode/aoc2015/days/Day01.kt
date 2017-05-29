package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override fun first(input: Sequence<String>) = input.first().toList()
            .groupBy { it }
            .map { if (it.key == '(') it.value.size else it.value.size * -1 }
            .sum()

    override fun second(input: Sequence<String>): Any {
        var floor = 0
        return input
                .first()
                .map { if (it == '(') 1 else -1 }
                .takeWhile {
                    floor += it
                    floor >= 0
                }.size
    }
}
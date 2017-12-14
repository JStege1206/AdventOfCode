package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.transpose

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day() {
    override suspend fun first(input: Sequence<String>): Any = input.toList()
            .map(String::toList)
            .transpose()
            .map { it.groupBy { it }.maxBy { it.value.size }?.key }
            .joinToString("")

    override suspend fun second(input: Sequence<String>): Any = input.toList()
            .map(String::toList)
            .transpose()
            .map { it.groupBy { it }.minBy { it.value.size }?.key }
            .joinToString("")
}

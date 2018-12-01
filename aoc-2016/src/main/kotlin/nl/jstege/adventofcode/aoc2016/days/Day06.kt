package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.transpose
import kotlin.reflect.KFunction1

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day(title = "Signals and Noise") {
    override fun first(input: Sequence<String>): Any = input.toList()
        .map(String::toList)
        .transpose()
        .map { column -> column.groupBy { it }.maxBy { it.value.size }?.key }
        .joinToString("")

    override fun second(input: Sequence<String>): Any = input.toList()
        .map(String::toList)
        .transpose()
        .map { column -> column.groupBy { it }.minBy { it.value.size }?.key }
        .joinToString("")

}

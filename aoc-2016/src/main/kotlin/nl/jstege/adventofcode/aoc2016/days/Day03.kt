package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.transpose


/**
 *
 * @author Jelle Stege
 */
class Day03 : Day(title = "Squares With Three Sides") {
    private companion object Configuration {
        private const val WHITESPACE_PATTERN_STRING = """\s+"""
        private val WHITESPACE_REGEX = WHITESPACE_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any = input
        .map { it.trim().split(WHITESPACE_REGEX).map(String::toInt) }
        .filter { (x, y, z) -> isValid(x, y, z) }
        .toList()
        .size

    override fun second(input: Sequence<String>): Any = input
        .map { it.trim().split(WHITESPACE_REGEX).map(String::toInt) }
        .toList()
        .transpose()
        .flatten()
        .chunked(3)
        .filter { (x, y, z) -> isValid(x, y, z) }
        .size

    private fun isValid(x: Int, y: Int, z: Int) = (x + y > z) && (x + z > y) && (y + z > x)
}

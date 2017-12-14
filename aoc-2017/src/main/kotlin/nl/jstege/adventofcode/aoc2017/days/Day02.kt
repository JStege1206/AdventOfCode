package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.combinations

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day() {
    private companion object Configuration {
        private const val WHITESPACE_STRING = """\s+"""
        private val WHITESPACE_REGEX = WHITESPACE_STRING.toRegex()
    }

    override suspend fun first(input: Sequence<String>): Any {
        return input.parse()
                .sumBy { it.max()!! - it.min()!! }
    }

    override suspend fun second(input: Sequence<String>): Any {
        return input.parse()
                .map {
                    it.sortedDescending()
                            .combinations(2)
                            .first { (a, n) -> a % n == 0 }
                }
                .sumBy { (a, n) -> a / n }
    }

    private fun Sequence<String>.parse() = this
            .map { it.split(WHITESPACE_REGEX) }
            .map { it.map(String::toInt) }
}

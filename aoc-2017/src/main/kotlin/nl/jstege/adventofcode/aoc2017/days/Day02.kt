package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.combinations

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day() {
    override fun first(input: Sequence<String>): Any {
        return input.parse()
                .map { it.max()!! - it.min()!! }
                .sum()
    }

    override fun second(input: Sequence<String>): Any {
        return input.parse().toList()
                .map {
                    it
                            .sortedDescending()
                            .combinations(2)
                            .first { (a, n) -> a % n == 0 }
                }
                .sumBy { (a, n) -> a / n }
    }

    private fun Sequence<String>.parse() = this
            .map { it.split("""\s+""".toRegex()) }
            .map { it.map(String::toInt) }
}

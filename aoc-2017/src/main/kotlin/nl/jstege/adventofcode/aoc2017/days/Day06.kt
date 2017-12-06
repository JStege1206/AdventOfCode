package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day() {
    private companion object Configuration {
        private const val WHITESPACE_STRING = """\s+"""
        private val WHITESPACE_REGEX = WHITESPACE_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any {
        return input.head
                .split(WHITESPACE_REGEX)
                .map(String::toInt)
                .toMutableList()
                .cycle()
                .first + 1
    }

    override fun second(input: Sequence<String>): Any {
        return input.head
                .split(WHITESPACE_REGEX)
                .map(String::toInt)
                .toMutableList()
                .cycle()
                .let { (currentCycle, previousCycle) -> currentCycle - previousCycle }
    }

    private fun MutableList<Int>.cycle(): Pair<Int, Int> {
        val states = mutableMapOf<List<Int>, Int>()
        return generateSequence { this }
                .map { banks ->
                    var (index, value) = banks.withIndex().maxBy { it.value }!!
                    banks[index] = 0
                    while (value-- > 0) banks[++index % banks.size]++
                    banks.toList()
                }
                .onEach { if (it !in states) states[it] = states.size + 1}
                .first { states.size != states[it]!! }
                .let { states.size to states[it]!! }
    }
}

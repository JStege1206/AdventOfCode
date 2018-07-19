package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day(title = "Memory Reallocation") {
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
            .first
    }

    override fun second(input: Sequence<String>): Any {
        return input.head
            .split(WHITESPACE_REGEX)
            .map(String::toInt)
            .toMutableList()
            .cycle()
            .run { first - second }
    }

    private fun MutableList<Int>.cycle(): Pair<Int, Int> {
        val states = mutableMapOf<List<Int>, Int>()
        return generateSequence { this }
            .map { banks ->
                // Note that banks is a reference to "this", the same reference is passed
                // to map { } every time, an update would therefore also update future
                // executions of this method.
                var (index, value) = banks.withIndex().maxBy { it.value }!!
                banks[index] = 0
                while (value-- > 0) banks[++index % banks.size]++

                // Copy the list and pass it on.
                banks.toList()
            }
            .onEach { states.getOrPut(it, { states.size + 1 }) }
            .first { states.size != states[it]!! }
            // Since we've encountered a double, states.size
            // is not equal to the current cycle index,
            // thus, increase it by one
            .let { states.size + 1 to states[it]!! }
    }
}

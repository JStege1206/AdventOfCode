package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day(title = "All in a Single Night") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = """(\w+) to (\w+) = (\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val FROM_INDEX = 1
        private const val TO_INDEX = 2
        private const val COST_INDEX = 3

        private val PARAM_INDICES = intArrayOf(FROM_INDEX, TO_INDEX, COST_INDEX)
    }

    override fun first(input: Sequence<String>): Any = input
        .parse()
        .determineCosts()
        .min()!!


    override fun second(input: Sequence<String>): Any = input
        .parse()
        .determineCosts()
        .max()!!


    private fun Sequence<String>.parse(): RouteMap = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .transformTo(RouteMap()) { routes, (from, to, cost) ->
            routes[from, to] = cost.toInt()
        }

    private class RouteMap {
        private val routes = mutableMapOf<String, MutableMap<String, Int>>()

        operator fun get(k1: String, k2: String): Int? = routes[k1, k2]

        operator fun set(k1: String, k2: String, v: Int) {
            routes[k1, k2] = v
            routes[k2, k1] = v
        }

        fun determineCosts(): Sequence<Int> =
            this.routes.keys.toList()
                .permutations()
                .map { it.zipWithNext { prev, current -> this[prev, current]!! }.sum() }
    }
}

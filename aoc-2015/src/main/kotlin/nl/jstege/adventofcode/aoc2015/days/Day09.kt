package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day() {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = """(\w+) to (\w+) = (\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any = input
            .parse().determineCosts().min()!!


    override fun second(input: Sequence<String>): Any = input
            .parse().determineCosts().max()!!


    private fun Sequence<String>.parse(): RouteMap = this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .fold(RouteMap()) { routes, (_, from, to, cost) ->
                routes[from, to] = cost.toInt()
                routes
            }

    private class RouteMap {
        private val routes = mutableMapOf<String, MutableMap<String, Int>>()

        operator fun get(k1: String, k2: String): Int? = routes[k1, k2]

        operator fun set(k1: String, k2: String, v: Int) {
            routes[k1, k2] = v
            routes[k2, k1] = v
        }

        fun determineCosts(): Sequence<Int> = this.routes.keys.toList()
                .permutations()
                .map {
                    it.tail.fold(0 to it.head) { (cost, prev), current ->
                        (cost + this[prev, current]!!) to current
                    }.first
                }
    }
}

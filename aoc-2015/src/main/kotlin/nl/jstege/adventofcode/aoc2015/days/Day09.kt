package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.permutations
import nl.jstege.adventofcode.aoccommon.utils.extensions.tail
import nl.jstege.adventofcode.aoccommon.utils.extensions.get
import nl.jstege.adventofcode.aoccommon.utils.extensions.set

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day() {
    val INPUT_REGEX = "(\\w+) to (\\w+) = (\\d+)".toRegex()
    override fun first(input: Sequence<String>) = input
            .parse().determineCosts().min()!!


    override fun second(input: Sequence<String>) = input
            .parse().determineCosts().max()!!


    private fun Sequence<String>.parse(): Map<String, Map<String, Int>> = this
            .map {
                INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
            }
            .fold(mutableMapOf<String, MutableMap<String, Int>>(),
                    { routes, (_, from, to, cost) ->
                        routes[from, to] = cost.toInt()
                        routes[to, from] = cost.toInt()
                        routes
                    })

    private fun Map<String, Map<String, Int>>.determineCosts() = this.keys.toList()
            .permutations()
            .map {
                it.tail.fold(0 to it.head, { (cost, prev), current ->
                    (cost + (this[prev, current])!!) to current
                }).first
            }
}
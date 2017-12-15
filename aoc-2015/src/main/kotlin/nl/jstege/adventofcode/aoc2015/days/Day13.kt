package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.permutations
import nl.jstege.adventofcode.aoccommon.utils.extensions.set
import nl.jstege.adventofcode.aoccommon.utils.extensions.get
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.last

/**
 *
 * @author Jelle Stege
 */
class Day13 : Day() {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = "([a-zA-Z]+) would (lose|gain) (\\d+) happiness " +
                "units by sitting next to ([a-zA-Z]+)\\."
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>) = input.parse().optimal()

    override fun second(input: Sequence<String>) = input.parse().addOwn().optimal()

    private fun Sequence<String>.parse(): Map<String, Map<String, Int>> = this
            .map {
                INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
            }
            .fold(mutableMapOf<String, MutableMap<String, Int>>())
            { relations, (_, p1, neg, amt, p2) ->
                relations[p1, p2] = amt.toInt() * if (neg == "lose") -1 else 1
                relations
            }

    private fun Map<String, Map<String, Int>>.addOwn() = this
            .map { (k, v) ->
                k to (v + Pair("", 0))
            }
            .toMap() + ("" to mapOf(*this.keys.map { it to 0 }.toTypedArray()))


    private fun Map<String, Map<String, Int>>.optimal() = this.keys.toList()
            .permutations()
            .map {
                val f = it.head
                val l = it.last
                it.fold(0 to f) { (sum, prev), cur ->
                    (sum + (this[prev, cur] ?: 0) + (this[cur, prev] ?: 0)) to cur
                }.first + (this[f, l] ?: 0) + (this[l, f] ?: 0)
            }.max()!!
}

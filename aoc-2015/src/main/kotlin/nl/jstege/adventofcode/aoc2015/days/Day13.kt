package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day13 : Day(title = "Knights of the Dinner Table") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = "([a-zA-Z]+) would (lose|gain) (\\d+) happiness " +
                "units by sitting next to ([a-zA-Z]+)\\."
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val PERSON1_INDEX = 1
        private const val NEGATE_INDEX = 2
        private const val AMOUNT_INDEX = 3
        private const val PERSON2_INDEX = 4

        private val PARAM_INDICES =
            intArrayOf(PERSON1_INDEX, NEGATE_INDEX, AMOUNT_INDEX, PERSON2_INDEX)
    }

    override fun first(input: Sequence<String>) = input.parse().optimal()

    override fun second(input: Sequence<String>) = input.parse().addOwn().optimal()

    private fun Sequence<String>.parse(): Map<String, Map<String, Int>> = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .transformTo(mutableMapOf<String, MutableMap<String, Int>>())
        { relations, (p1, neg, amt, p2) ->
            relations[p1, p2] = amt.toInt() * if (neg == "lose") -1 else 1
        }

    private fun Map<String, Map<String, Int>>.addOwn() = this
        .map { (k, v) -> Pair(k, v + Pair("", 0)) }
        .toMap() + ("" to mapOf(*this.keys.map { it to 0 }.toTypedArray()))


    private fun Map<String, Map<String, Int>>.optimal() = this.keys.toList()
        .permutations()
        .map {
            it.zipWithNext { prev, cur ->
                (this[prev, cur] ?: 0) + (this[cur, prev] ?: 0)
            }.sum() + (this[it.head, it.last] ?: 0) + (this[it.last, it.head] ?: 0)
        }.max()!!
}

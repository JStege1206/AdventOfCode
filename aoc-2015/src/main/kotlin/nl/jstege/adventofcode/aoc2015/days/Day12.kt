package nl.jstege.adventofcode.aoc2015.days

import com.fasterxml.jackson.databind.JsonNode
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.toJson

/**
 *
 * @author Jelle Stege
 */
class Day12 : Day() {
    override fun first(input: Sequence<String>): Any = input.toJson().sum()

    override fun second(input: Sequence<String>): Any = input.toJson().sumWithoutRed()

    private fun JsonNode.sum(): Int {
        if (this.isInt) return this.asInt()
        return this.sumBy { it.sum() }
    }

    private fun JsonNode.sumWithoutRed(): Int {
        if (this.isInt) return this.asInt()

        if (this.any { it.isTextual && it.asText() == "red" && this.isObject }) {
            return 0
        }
        return this.sumBy { it.sumWithoutRed() }
    }
}
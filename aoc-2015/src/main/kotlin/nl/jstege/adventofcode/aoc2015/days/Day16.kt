package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day


/**
 *
 * @author Jelle Stege
 */
class Day16 : Day() {
    private val TARGET_AUNT = Aunt.parse(
            "Sue 0: children: 3, cats: 7, samoyeds: 2, pomeranians: 3, akitas: 0, " +
                    "vizslas: 0, goldfish: 5, trees: 3, cars: 2, perfumes: 1"
    )

    override fun first(input: Sequence<String>) = input
            .map(Aunt.Companion::parse)
            .first { it.matches(TARGET_AUNT, false) }.nr

    override fun second(input: Sequence<String>) = input
            .map(Aunt.Companion::parse)
            .first { it.matches(TARGET_AUNT, true) }.nr

    private class Aunt private constructor(val nr: Int, val compounds: Map<String, Int>) {
        operator fun get(compound: String): Int = compounds[compound] ?: Int.MIN_VALUE

        fun matches(target: Aunt, useRanges: Boolean): Boolean = POSSIBLE_COMPOUNDS
                .all { matches(target, it, useRanges) }

        fun matches(target: Aunt, compound: String, useRanges: Boolean): Boolean {
            if (this[compound] == Int.MIN_VALUE || target[compound] == Int.MIN_VALUE) {
                return true
            }
            if (useRanges && compound in GREATER_THAN_COMPOUNDS) {
                return this[compound] > target[compound]
            }
            if (useRanges && compound in FEWER_THAN_COMPOUNDS) {
                return this[compound] < target[compound]
            }
            return this[compound] == target[compound]
        }

        companion object {
            @JvmStatic private val POSSIBLE_COMPOUNDS = setOf(
                    "children",
                    "cats",
                    "samoyeds",
                    "pomeranians",
                    "akitas",
                    "vizslas",
                    "goldfish",
                    "trees",
                    "cars",
                    "perfumes"
            )

            @JvmStatic private val GREATER_THAN_COMPOUNDS = setOf(
                    "cats",
                    "trees"
            )

            @JvmStatic private val FEWER_THAN_COMPOUNDS = setOf(
                    "pomeranians",
                    "goldfish"
            )

            @JvmStatic fun parse(input: String): Aunt {
                val parts = input.split(": ", limit = 2)
                return Aunt(parts[0].substring(4).toInt(), parts[1]
                        .split(", ")
                        .map {
                            val (k, v) = it.split(": ")
                            k to v.toInt()
                        }.toMap()
                )
            }
        }
    }
}
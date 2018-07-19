package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day


/**
 *
 * @author Jelle Stege
 */
class Day16 : Day(title = "Aunt Sue") {
    private companion object Configuration {
        private const val TARGET_AUNT_STRING = "Sue 0: " +
                "children: 3, " +
                "cats: 7, " +
                "samoyeds: 2, " +
                "pomeranians: 3, " +
                "akitas: 0, " +
                "vizslas: 0, " +
                "goldfish: 5, " +
                "trees: 3, " +
                "cars: 2, " +
                "perfumes: 1"
        private val TARGET_AUNT = Aunt.parse(TARGET_AUNT_STRING)
    }

    override fun first(input: Sequence<String>) = input
        .map(Aunt.Companion::parse)
        .first { it.matches(TARGET_AUNT, false) }.id

    override fun second(input: Sequence<String>) = input
        .map(Aunt.Companion::parse)
        .first { it.matches(TARGET_AUNT, true) }.id

    private class Aunt(val id: Int, val compounds: Map<Compound, Int>) {
        fun matches(other: Aunt, useModifier: Boolean) = compounds
            .keys
            .all { matches(other, useModifier, it) }

        fun matches(other: Aunt, useModifier: Boolean, c: Compound) = when {
            other.compounds[c] == null -> true
            useModifier -> c.modifier(this.compounds[c]!!, other.compounds[c]!!)
            else -> this.compounds[c]!! == other.compounds[c]!!
        }

        companion object {
            fun parse(input: String): Aunt = input
                .split(": ", limit = 2)
                .let { (id, compounds) ->
                    Aunt(id.substring(4).toInt(), compounds.split(", ").associate {
                        it.split(": ").let { (k, v) -> Compound.fromString(k) to v.toInt() }
                    })
                }
        }
    }

    private enum class Compound(val modifier: ((Int, Int) -> Boolean)) {
        CHILDREN({ i, j -> i == j }),
        CATS({ i, j -> i > j }),
        SAMOYEDS({ i, j -> i == j }),
        POMERANIANS({ i, j -> i < j }),
        AKITAS({ i, j -> i == j }),
        VIZSLAS({ i, j -> i == j }),
        GOLDFISH({ i, j -> i < j }),
        TREES({ i, j -> i > j }),
        CARS({ i, j -> i == j }),
        PERFUMES({ i, j -> i == j });

        companion object {
            fun fromString(s: String) = Compound.valueOf(s.toUpperCase())
        }
    }
}

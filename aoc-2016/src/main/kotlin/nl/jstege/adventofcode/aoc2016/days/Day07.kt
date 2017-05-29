package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day() {
    private val HYPERNET = true
    private val NOT_HYPERNET = false

    override fun first(input: Sequence<String>): Any = input
            .map {
                val segments = it.getIpv7Segments()
                segments[HYPERNET]!!.none { it.hasAbbaSequence() }
                        && segments[NOT_HYPERNET]!!.any { it.hasAbbaSequence() }
            }.count { it }

    override fun second(input: Sequence<String>): Any = input
            .map {
                val segments = it.getIpv7Segments()
                val possibleBabs = segments[NOT_HYPERNET]!!
                        .flatMap { it.calculateAbas() }
                        .map { it.calculateBab() }
                segments[HYPERNET]!!
                        .map { s -> possibleBabs.any { s.contains(it) } }
                        .any { it }
            }.count { it }


    private fun String.getIpv7Segments(): Map<Boolean, List<String>> {
        val segments = mapOf<Boolean, MutableList<String>>(
                HYPERNET to mutableListOf(),
                NOT_HYPERNET to mutableListOf()
        )
        var isHypernet = NOT_HYPERNET
        segments[isHypernet]!! += this.fold(StringBuilder(), { p, it ->
            if (it == '[' || it == ']') {
                if (p.isNotEmpty()) {
                    segments[isHypernet]!! += p.toString()
                    isHypernet = it == '['
                }
                StringBuilder()
            } else {
                p.append(it)
            }

        }).toString()

        return segments
    }

    private fun String.hasAbbaSequence(): Boolean = (0 until this.length - 3)
            .any {
                this[it] != this[it + 1]
                        && this[it] == this[it + 3]
                        && this[it + 1] == this[it + 2]
            }

    private fun String.calculateAbas(): Set<String> = (0 until this.length - 2)
            .filter { this[it] == this[it + 2] && this[it] != this[it + 1] }
            .map { String(charArrayOf(this[it], this[it + 1], this[it])) }.toSet()

    private fun String.calculateBab(): String = String(charArrayOf(this[1], this[0], this[1]))
}
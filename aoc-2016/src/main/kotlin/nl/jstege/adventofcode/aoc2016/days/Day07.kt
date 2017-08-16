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
                segments[HYPERNET].none { it.hasAbbaSequence() }
                        && segments[NOT_HYPERNET].any { it.hasAbbaSequence() }
            }.count { it }

    override fun second(input: Sequence<String>): Any = input
            .map {
                val segments = it.getIpv7Segments()
                val possibleBabs = segments[NOT_HYPERNET]
                        .flatMap { it.calculateAbas() }
                        .map { it.calculateBab() }
                segments[HYPERNET]
                        .map { s -> possibleBabs.any { it in s } }
                        .any { it }
            }.count { it }

    private fun String.getIpv7Segments(): Ipv7Segments {
        val segments = Ipv7Segments()

        var isHypernet = false
        segments[isHypernet].add(this.fold(StringBuilder()) { p, it ->
            if (it == '[' || it == ']') {
                if (p.isNotEmpty()) {
                    segments[isHypernet].add(p.toString())
                    isHypernet = it == '['
                }
                StringBuilder()
            } else {
                p.append(it)
            }
        }.toString())
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
            .map { "${this[it]}${this[it + 1]}${this[it]}" }.toSet()

    private fun String.calculateBab(): String = "${this[1]}${this[0]}${this[1]}"

    data class Ipv7Segments(
            var hypernet: MutableSet<String> = mutableSetOf(),
            var notHypernet: MutableSet<String> = mutableSetOf()
    ) {
        operator fun get(hypernet: Boolean) = if (hypernet) this.hypernet else notHypernet
    }
}
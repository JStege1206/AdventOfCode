package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day(title = "Internet Protocol Version 7") {
    override fun first(input: Sequence<String>): Any = input.asSequence()
        .count { ipv7 ->
            ipv7.getIpv7Segments().run {
                hypernet.none { it.hasAbbaSequence() }
                        && notHypernet.any { it.hasAbbaSequence() }
            }
        }

    override fun second(input: Sequence<String>): Any = input.asSequence()
        .count { ipv7 ->
            ipv7.getIpv7Segments().run {
                val possibleBabs = notHypernet
                    .flatMap { it.calculateAbas() }
                    .map { it.calculateBab() }
                hypernet.any { s -> possibleBabs.any { it in s } }
            }
        }

    private fun String.getIpv7Segments(): Ipv7Segments {
        val segments = Ipv7Segments()
        val tokenizer = StringTokenizer(this, "[]", true)

        var isHyperNet = false
        var previous = ""
        while (tokenizer.hasMoreTokens()) {
            when (val token = tokenizer.nextToken()) {
                "[", "]" -> {
                    if (previous.isNotEmpty()) {
                        if (isHyperNet) segments.hypernet.add(previous)
                        else segments.notHypernet.add(previous)
                    }
                    isHyperNet = token == "["
                    previous = ""
                }
                else -> previous = token
            }
        }

        if (previous.isNotEmpty()) {
            if (isHyperNet) segments.hypernet.add(previous)
            else segments.notHypernet.add(previous)
        }
        return segments
    }

    private fun String.hasAbbaSequence(): Boolean = (0 until this.length - 3)
        .any {
            this[it] != this[it + 1] && this[it] == this[it + 3] && this[it + 1] == this[it + 2]
        }

    private fun String.calculateAbas(): Set<String> = (0 until this.length - 2)
        .filter { this[it] == this[it + 2] && this[it] != this[it + 1] }
        .map { this.substring(it, it + 2) }
        .toSet()

    private fun String.calculateBab(): String = "${this[1]}${this[0]}${this[1]}"

    private data class Ipv7Segments(
        val hypernet: MutableSet<String> = mutableSetOf(),
        val notHypernet: MutableSet<String> = mutableSetOf()
    )
}

package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

class Day08 : Day(title = "Memory Maneuver") {
    override fun first(input: Sequence<String>): Any =
        input.head
            .parse()
            .sumMetadata()

    override fun second(input: Sequence<String>): Any =
        input.head
            .parse()
            .calculateNodeValues()
            .sum()

    fun String.parse(): Iterator<Int> = object : Iterator<Int> {
        val iterator = this@parse.trim().iterator()
        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): Int {
            var n = 0
            var c = iterator.nextChar()
            do {
                n = n * 10 + (c - '0')
                if (iterator.hasNext()) c = iterator.nextChar()
            } while (c != ' ' && iterator.hasNext())

            return n
        }
    }

    private fun Iterator<Int>.sumMetadata(): Int =
        (next() to next())
            .let { (children, metadata) ->
                (0 until children)
                    .sumBy { sumMetadata() } + (0 until metadata).sumBy { next() }
            }

    private fun Iterator<Int>.calculateNodeValues(): List<Int> =
        (next() to next())
            .let { (children, metadata) ->
                if (children == 0)
                    (0 until metadata).map { next() }
                else
                    (0 until children)
                        .map { calculateNodeValues() }
                        .slice((0 until metadata).map { next() - 1 }.filter { it < children })
                        .flatten()
            }
}

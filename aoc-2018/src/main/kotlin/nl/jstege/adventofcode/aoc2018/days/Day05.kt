package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import java.util.*

class Day05 : Day(title = "Alchemical Reduction") {
    override fun first(input: Sequence<String>): Any =
        input.head.collapse()

    override fun second(input: Sequence<String>): Any =
        ('a'..'z').map { c ->
            input.head
                .filterNot { it == c.toLowerCase() || it == c.toUpperCase() }
                .collapse()
        }.min() ?: throw IllegalStateException()

    private fun Char.oppositeOf(other: Char) =
        this - other == 32 || this - other == -32

    private fun String.collapse() = this
        .fold(ArrayDeque<Char>()) { s, current ->
            if (s.isNotEmpty() && current.oppositeOf(s.peek())) s.apply { removeFirst() }
            else s.apply { addFirst(current) }
        }.size
}

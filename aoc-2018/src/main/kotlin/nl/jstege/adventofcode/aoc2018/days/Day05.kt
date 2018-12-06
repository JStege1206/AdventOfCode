package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import java.util.*

class Day05 : Day(title = "Alchemical Reduction") {
    override fun first(input: Sequence<String>): Any =
        input.head
            .run { collapsed(initialStackSize = length) }
            .size

    override fun second(input: Sequence<String>): Any = input.head
        .collapsed()
        .joinToString("")
        .let { newPolymer ->
            ('a'..'z')
                .asSequence()
                .filter { it in newPolymer || it.toUpperCase() in newPolymer }
                .map { c -> newPolymer.collapsed(c, newPolymer.length).size }
                .min() ?: throw IllegalStateException()
        }


    private fun Char.oppositeOf(other: Char?): Boolean =
        other?.minus(this) == 32 || other?.minus(this) == -32

    private fun String.collapsed(lowerCaseBadChar: Char? = null, initialStackSize: Int = 10000) =
        this.fold(ArrayDeque<Char>(initialStackSize)) { stack, current ->
            when {
                lowerCaseBadChar != null && current.toLowerCase() == lowerCaseBadChar -> Unit
                current.oppositeOf(stack.peek()) -> stack.removeFirst()
                else -> stack.addFirst(current)
            }
            stack
        }
}

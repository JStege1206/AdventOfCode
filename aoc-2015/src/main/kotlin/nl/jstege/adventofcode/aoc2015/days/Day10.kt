package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day(title = "Elves Look, Elves Say") {
    private companion object Configuration {
        private const val FIRST_ITERATIONS = 40
        private const val SECOND_ITERATIONS = 50
    }

    override fun first(input: Sequence<String>): Any = input.head
        .map { it - '0' }
        .iterate(FIRST_ITERATIONS)
        .size

    override fun second(input: Sequence<String>): Any = input.head
        .map { it - '0' }
        .iterate(SECOND_ITERATIONS)
        .size

    private fun List<Int>.iterate(iterations: Int): List<Int> = (0 until iterations)
        .fold(this) { old, _ -> old.calculateNew() }

    private tailrec fun List<Int>.calculateNew(
        index: Int = 0,
        acc: MutableList<Int> = mutableListOf()
    ): List<Int> =
        if (index >= size) acc
        else {
            val nr = this[index]
            val amount = calculateNextIndex(index, nr)

            calculateNew(index + amount, acc.apply { add(amount); add(nr) })
        }

    private tailrec fun List<Int>.calculateNextIndex(index: Int, v: Int, acc: Int = 1): Int =
        if ((index + acc) >= size || this[index + acc] != v) acc
        else calculateNextIndex(index, v, acc + 1)
}

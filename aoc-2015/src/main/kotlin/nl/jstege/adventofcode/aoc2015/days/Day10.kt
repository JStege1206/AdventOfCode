package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day() {
    private companion object Configuration {
        private const val FIRST_ITERATIONS = 40
        private const val SECOND_ITERATIONS = 50
    }

    override val title: String = "Elves Look, Elves Say"

    override fun first(input: Sequence<String>): Any = input.head
            .map { it - '0' }
            .iterate(FIRST_ITERATIONS)
            .size

    override fun second(input: Sequence<String>): Any = input.head
            .map { it - '0' }
            .iterate(SECOND_ITERATIONS)
            .size

    private fun List<Int>.iterate(iterations: Int): List<Int> = (0 until iterations)
            .fold(this.toList()) { old, _ ->
                val new = mutableListOf<Int>()
                var j = 0
                while (j < old.size) {
                    val nr = old[j]
                    var k = 1
                    while ((j + k) < old.size && old[j + k] == nr) {
                        k++
                    }
                    new += k
                    new += nr
                    j += k
                }
                new
            }
}

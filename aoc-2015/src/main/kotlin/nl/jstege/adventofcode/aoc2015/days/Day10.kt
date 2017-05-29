package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day() {
    private val FIRST_ITERATIONS = 40
    private val SECOND_ITERATIONS = 50
    override fun first(input: Sequence<String>) = input.first()
            .map { it - '0' }
            .iterate(FIRST_ITERATIONS)
            .size

    override fun second(input: Sequence<String>) = input.first()
            .map { it - '0' }
            .iterate(SECOND_ITERATIONS)
            .size

    private fun List<Int>.iterate(iterations: Int) = (0 until iterations)
            .fold(this.toMutableList(), { old, _ ->
                val new = mutableListOf<Int>()
                var j = 0
                while (j < old.size) {
                    val nr = old[j]
                    var k = 1
                    while ((j + k) < old.size && old[j + k] == nr) {
                        k++
                    }
                    new.add(k)
                    new.add(nr)
                    j += k
                }
                new
            })
}
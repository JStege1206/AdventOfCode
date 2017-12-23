package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day24 : Day() {
    private companion object Configuration {
        val FIRST_SHARES = 3
        val SECOND_SHARES = 4
    }

    override val title: String = "It Hangs in the Balance"

    override fun first(input: Sequence<String>): Any = input
            .map { it.toInt() }
            .sorted()
            .toList()
            .findQuantumEntanglement(FIRST_SHARES)

    override fun second(input: Sequence<String>): Any = input
            .map { it.toInt() }
            .sorted()
            .toList()
            .findQuantumEntanglement(SECOND_SHARES)

    private fun List<Int>.findQuantumEntanglement(groups: Int): Long {
        val groupSize = this.sum() / groups
        val start = this
                .asSequence()
                .sortedDescending()
                .scan(0, Int::plus)
                .takeWhile { it < groupSize }
                .count()
        return (start until this.size)
                .asSequence()
                .map { this.combinations(it).filter { it.sum() == groupSize } }
                .filter(Sequence<List<Int>>::any)
                .orElse { sequenceOf(listOf(Int.MAX_VALUE)) }
                .map { it.map(Int::toLong) }
                .map { it.reduce(Long::times) }
                .min()!!
    }

}

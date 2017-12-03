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
                .scan(0 to listOf<Int>()) { (sum, r), i ->
                    sum + i to r + i
                }
                .takeWhile { (sum, _) -> sum < groupSize }
                .toList().size
        return (start until this.size)
                .asSequence()
                .map { this.combinations(it).filter { it.sum() == groupSize } }
                .filter { it.any() }
                .orElse { listOf(listOf(Int.MAX_VALUE)).asSequence() }
                .map { it.map { it.toLong() } }
                .map { it.reduce(Long::times) }
                .min()!!
    }

}

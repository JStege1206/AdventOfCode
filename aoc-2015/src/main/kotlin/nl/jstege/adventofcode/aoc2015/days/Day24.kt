package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day24 : Day(title = "It Hangs in the Balance") {
    private companion object Configuration {
        const val FIRST_SHARES = 3
        const val SECOND_SHARES = 4
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

    private fun List<Int>.findQuantumEntanglement(groups: Int): Long = (this.sum() / groups)
        .let { groupSize ->
            this
                .asSequence()
                .sortedDescending()
                .scan(0, Int::plus)
                .takeWhile { it < groupSize }
                .count()
                .let { start ->
                    (start until this.size)
                        .asSequence()
                        .map { this.combinations(it).filter { it.sum() == groupSize } }
                        .filter { it.any() }
                        .filter(Sequence<Set<Int>>::any)
                        .first()
                        .map { it.map(Int::toLong) }
                        .map { it.reduce(Long::times) }
                        .min()!!
                }
        }
}

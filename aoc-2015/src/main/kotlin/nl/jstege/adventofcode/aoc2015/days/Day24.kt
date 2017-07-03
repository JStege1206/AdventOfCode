package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day24 : Day() {
    val FIRST_SHARES = 3
    val SECOND_SHARES = 4
    override fun first(input: Sequence<String>): Any = input
            .map { it.toInt() }
            .sorted()
            .toList()
            .findQe(FIRST_SHARES)

    override fun second(input: Sequence<String>): Any = input
            .map { it.toInt() }
            .sorted()
            .toList()
            .findQe(SECOND_SHARES)

    fun List<Int>.findQe(groups: Int): Long {
        val groupSize = this.sum() / groups
        val start = this.sortedDescending().takeWhileSumLessThan(groupSize).size
        return (start until this.size).asSequence()
                .map {
                    this.combinations(it).filter { it.sum() == groupSize }
                }
                .filter { it.any() }
                .firstOrElse { listOf(listOf(Int.MAX_VALUE)).asSequence() }
                .map { it.fold(1L, Long::times)}
                .min()!!
    }

}
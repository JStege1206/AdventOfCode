package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day


/**
 *
 * @author Jelle Stege
 */
class Day17 : Day() {
    private companion object Configuration {
        private const val CAPACITY = 150
    }

    override fun first(input: Sequence<String>) = input
            .map { it.toInt() }
            .toList()
            .findCombinations(CAPACITY)

    override fun second(input: Sequence<String>): Any {
        val containers = input.map { it.toInt() }.toList()

        val depths = mutableListOf<Int>()
        containers.findCombinations(CAPACITY, depths = depths)
        val min = depths.min()
        return depths.count { it == min }
    }


    private fun List<Int>.findCombinations(capacityLeft: Int, depth: Int = 1,
                                           depths: MutableList<Int> = mutableListOf()): Int {
        if (capacityLeft > 0 && this.isEmpty()) {
            return 0
        }

        var combinations = 0

        val tContainers = this.toMutableList()
        while (tContainers.isNotEmpty()) {
            val container = tContainers.removeAt(0)
            if (capacityLeft - container == 0) {
                depths.add(depth)
                combinations++
            } else {
                combinations += tContainers.findCombinations(capacityLeft - container,
                        depth + 1, depths)
            }
        }
        return combinations
    }
}

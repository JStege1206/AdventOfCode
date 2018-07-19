package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day


/**
 *
 * @author Jelle Stege
 */
class Day17 : Day(title = "No Such Thing as Too Much") {
    private companion object Configuration {
        private const val CAPACITY = 150
    }

    override fun first(input: Sequence<String>) = input
        .map { it.toInt() }
        .toList()
        .findCombinations(CAPACITY)

    override fun second(input: Sequence<String>): Any {
        return input.map { it.toInt() }.toList()
            .let { containers ->
                mutableListOf<Int>().let { depths ->
                    containers.findCombinations(CAPACITY, depths = depths)
                    depths.min().let { min -> depths.count { it == min } }
                }
            }
    }


    private fun List<Int>.findCombinations(
        capacityLeft: Int,
        depth: Int = 1,
        depths: MutableList<Int> = mutableListOf()
    ): Int {
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
                combinations += tContainers.findCombinations(
                    capacityLeft - container,
                    depth + 1, depths
                )
            }
        }
        return combinations
    }
}

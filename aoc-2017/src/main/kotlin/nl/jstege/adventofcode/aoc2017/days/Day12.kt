package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day12 : Day() {
    override fun first(input: Sequence<String>): Any {
        return countRoutes(0, input.buildConnections(), mutableSetOf())
    }

    override fun second(input: Sequence<String>): Any {
        val visited = mutableSetOf<Int>()
        val pipes = input.buildConnections()

        var groups = 0
        while (true) {
            val f = pipes.keys.find { it !in visited } ?: break
            countRoutes(f, pipes, visited)
            groups++
        }
        return groups
    }

    private fun countRoutes(pipe: Int, pipes: Map<Int, Set<Int>>, visited: MutableSet<Int>): Int {
        if (pipe in visited) return 0
        visited += pipe
        return 1 + pipes[pipe]!!
                .asSequence()
                .filter { it !in visited }
                .sumBy { countRoutes(it, pipes, visited) }
    }

    private fun Sequence<String>.buildConnections(): Map<Int, Set<Int>> = this
            .map { it.split(" <-> ") }
            .map { it.first().toInt() to it[1].split(", ").map { it.toInt() }.toSet() }
            .toMap()
}

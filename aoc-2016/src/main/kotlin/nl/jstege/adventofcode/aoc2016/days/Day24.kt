package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.permutations
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day24 : Day(title = "Air Duct Spelunking") {
    private companion object Configuration {
        private const val START_NODE = 0
        private const val END_NODE = 0
        private const val DEFAULT_SIZE = 300
    }

    override fun first(input: Sequence<String>): Any = input.toList()
        .findShortestRoute { path -> path }

    override fun second(input: Sequence<String>): Any = input.toList()
        .findShortestRoute { path -> path + END_NODE }


    private fun List<String>.findShortestRoute(pathModifier: (List<Int>) -> List<Int>): Int {
        val goals = mutableMapOf<Int, Int>()
        val maze = Maze(
            this
                .asSequence()
                .flatMap(String::asSequence)
                .withIndex()
                .transformTo(BitSet(this.size * this.head.length)) { bs, (i, c) ->
                    if (c != '#') {
                        if (c.isDigit()) {
                            goals[c - '0'] = i
                        }
                        bs.set(i)
                    }
                }, this.head.length
        )

        return goals.keys
            .filter { it != START_NODE }
            .permutations()
            .map { pathModifier(it) }
            .map { path -> path.map { goals[it]!! } }
            .map { path ->
                (listOf(goals[START_NODE]!!) + path)
                    .zipWithNext { prev, cur -> maze.findShortestPath(prev, cur) }
                    .sum()
            }.min() ?: throw IllegalArgumentException("No shortest route available")
    }

    private data class Path(val i: Int, val j: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Path) return false
            return (i == other.i && j == other.j) || (i == other.j && j == other.i)
        }

        override fun hashCode() = 31 * i * j
    }

    private inner class Maze(val maze: BitSet, val width: Int) {
        private val cache = mutableMapOf<Path, Int>()

        operator fun get(c: Int): Boolean = maze[c]

        operator fun set(c: Int, v: Boolean) {
            maze[c] = v
        }

        fun findShortestPath(from: Int, to: Int): Int {
            if (Path(from, to) in cache) {
                return cache[Path(from, to)]!!
            }
            val queue = ArrayDeque<Pair<Int, Int>>(DEFAULT_SIZE)
            queue.add(Pair(from, 0))
            val visited = mutableSetOf<Int>()
            var result = -1
            while (queue.isNotEmpty()) {
                val (c, d) = queue.removeFirst()
                if (c in visited) {
                    continue
                }
                visited += c
                if (c == to) {
                    result = d
                    break
                }
                if (c % width != 0 && maze[c - 1]) {
                    queue.add(Pair(c - 1, d + 1))
                }
                if (c + 1 % width != 0 && maze[c + 1]) {
                    queue.add(Pair(c + 1, d + 1))
                }
                if (c >= width && maze[c - width]) {
                    queue.add(Pair(c - width, d + 1))
                }
                if (c < maze.size() - width && maze[c + width]) {
                    queue.add(Pair(c + width, d + 1))
                }
            }
            if (result != -1) {
                cache[Path(from, to)] = result
                return result
            }
            throw IllegalArgumentException("No path available from $from to $to")

        }
    }
}

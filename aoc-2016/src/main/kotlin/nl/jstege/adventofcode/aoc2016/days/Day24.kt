package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.permutations
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day24 : Day() {
    private val START_NODE = 0
    private val END_NODE = 0
    private val DEFAULT_SIZE = 300

    override fun first(input: Sequence<String>): Any = input.toList()
            .findShortestRoute { it }

    override fun second(input: Sequence<String>): Any = input.toList()
            .findShortestRoute { it + listOf(END_NODE) }


    fun List<String>.findShortestRoute(pathModifier: (List<Int>) -> List<Int>): Int {
        val goals = mutableMapOf<Int, Int>()
        val maze = Maze(this
                .asSequence()
                .flatMap(String::asSequence)
                .foldIndexed(BitSet(this.size * this.head.length)) { i, bs, c ->
                    if (c != '#') {
                        if (c.isDigit()) {
                            goals[c - '0'] = i
                        }
                        bs.set(i)
                    }
                    bs
                }, this.head.length)

        return goals.keys
                .filter { it != START_NODE }
                .permutations()
                .map { pathModifier(it) }
                .map { it.map { goals[it]!! } }
                .map {
                    it.fold(0 to goals[START_NODE]!!) { (length, previousGoal), p ->
                        (length + maze.findShortestPath(previousGoal, p)) to  p
                    }.first
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

        operator fun get(c: Int): Boolean {
            return maze[c]
        }

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
package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoc2017.utils.hash.knot.KnotHash.knotHash
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.bitCount
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.parallelMap
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    companion object Configuration {
        const val GRID_HEIGHT = 128
        const val GRID_WIDTH = 128
    }

    override suspend fun first(input: Sequence<String>): Any {
        return "${input.head}-%d".let { stringInput ->
            (0 until GRID_HEIGHT).toList().parallelMap { it ->
                stringInput
                        .format(it)
                        .knotHash()
                        .sumBy { (it.toInt() and 0xFF).bitCount() }
            }.sum()
        }
    }

    override suspend fun second(input: Sequence<String>): Any {
        return "${input.head}-%d"
                .let { stringInput ->
                    val grid = (0 until GRID_HEIGHT).toList().parallelMap {
                        BitSet.valueOf(stringInput.format(it).knotHash().reversedArray())
                    }

                    Point.of(0 until GRID_WIDTH, 0 until GRID_HEIGHT)
                            .fold(mutableSetOf<Point>() to 0) { (visited, group), p ->
                                if (grid[p] && p !in visited) {
                                    p.findSetNeighbors(grid, visited) to group + 1
                                } else {
                                    visited to group
                                }
                            }.second
                }
    }

    operator fun List<BitSet>.get(p: Point) = this[p.y][p.x]

    fun Point.findSetNeighbors(grid: List<BitSet>, visited: MutableSet<Point>): MutableSet<Point> {
        val queue = ArrayDeque(listOf(this))
        while (queue.isNotEmpty()) {
            val el = queue.pop()
            visited += el
            queue.addAll(el.validNeighbors(grid, visited))
        }
        return visited
    }

    fun Point.validNeighbors(grid: List<BitSet>, visited: Set<Point>) = this
            .neighbors4
            .filter { grid.pointIsValid(it) && it !in visited }

    fun List<BitSet>.pointIsValid(el: Point) =
            el.y in (0 until GRID_HEIGHT) && el.x in (0 until GRID_WIDTH) && this[el]

}

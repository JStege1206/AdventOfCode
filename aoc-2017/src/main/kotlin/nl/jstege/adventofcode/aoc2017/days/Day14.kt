package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoc2017.utils.hash.knot.KnotHash.knotHash
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.bitCount
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.parallelMap
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    private companion object Configuration {
        const val GRID_HEIGHT = 128
        const val GRID_WIDTH = 128
    }

    override fun first(input: Sequence<String>): Any {
        return "${input.head}-%d"
                .calculateHashes { it.sumBy { it.toUnsignedInt().bitCount() } }
                .sum()
    }

    override fun second(input: Sequence<String>): Any {
        return "${input.head}-%d"
                .calculateHashes(ByteArray::reversedArray)
                .map(BitSet::valueOf)
                .let { grid ->
                    Point.of(0 until GRID_WIDTH, 0 until GRID_HEIGHT)
                            .asSequence()
                            .filter { grid[it] }
                            .fold(mutableSetOf<Point>() to 0) { (visited, group), p ->
                                if (p !in visited) {
                                    p.findGroupRec(grid, visited) to group + 1
                                } else {
                                    visited to group
                                }
                            }.second
                }
    }

    private fun <E> String.calculateHashes(transform: (ByteArray) -> E): List<E> =
            (0 until GRID_HEIGHT).parallelMap { transform(this.format(it).knotHash()) }


    private fun Point.findGroupRec(grid: List<BitSet>, visited: MutableSet<Point>): MutableSet<Point> {
        visited += this
        this.neighbors4
                .filter { grid.isValid(it) && it !in visited }
                .forEach { it.findGroupRec(grid, visited) }
        return visited
    }

    private operator fun List<BitSet>.get(p: Point) = this[p.y][p.x]

    private fun List<BitSet>.isValid(el: Point) =
            el.y in (0 until GRID_HEIGHT)
                    && el.x in (0 until GRID_WIDTH)
                    && this[el]

}

package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo
import java.util.*

/**
 *
 * @author Jelle Stege
 */
class Day18 : Day(title = "Like a GIF For Your Yard") {
    private companion object Configuration {
        private const val ITERATIONS = 100
    }

    override fun first(input: Sequence<String>) = (0 until ITERATIONS)
        .transformTo(Grid.parse(input.toList())) { grid, _ ->
            grid.getTogglePoints().forEach { (x, y) -> grid[x, y] = !grid[x, y] }
        }
        .cardinality()

    override fun second(input: Sequence<String>) = (0 until ITERATIONS)
        .transformTo(Grid.parse(input.toList())) { grid, _ ->
            grid.getTogglePoints().asSequence()
                .filterNot { (x, y) ->
                    (y == 0 || y == grid.height - 1) && (x == 0 || x == grid.width - 1)
                }
                .forEach { (x, y) -> grid[x, y] = !grid[x, y] }
        }
        .cardinality()


    private class Grid private constructor(val grid: BitSet, val height: Int, val width: Int) {
        operator fun get(x: Int, y: Int) = grid[y * width + x]
        operator fun set(x: Int, y: Int, value: Boolean) = grid.set(y * width + x, value)
        fun cardinality() = grid.cardinality()

        fun getTogglePoints(): Set<Point> = (0 until height)
            .flatMap { y ->
                (0 until width).map { x ->
                    Point.of(x, y)
                }
            }
            .filter { (x, y) ->
                countNeighbours(x, y)
                    .let { (this[x, y] && !(it == 2 || it == 3)) || (!this[x, y] && it == 3) }
            }
            .toSet()


        private fun countNeighbours(x: Int, y: Int): Int {
            var n = 0
            if (x > 0) {
                if (y > 0) {
                    n += if (this[x - 1, y - 1]) 1 else 0
                }
                n += if (this[x - 1, y]) 1 else 0
                if (y < height - 1) {
                    n += if (this[x - 1, y + 1]) 1 else 0
                }
            }
            if (y > 0) {
                n += if (this[x, y - 1]) 1 else 0
            }
            if (y < height - 1) {
                n += if (this[x, y + 1]) 1 else 0
            }
            if (x < width - 1) {
                if (y > 0) {
                    n += if (this[x + 1, y - 1]) 1 else 0
                }
                n += if (this[x + 1, y]) 1 else 0
                if (y < height - 1) {
                    n += if (this[x + 1, y + 1]) 1 else 0
                }
            }
            return n
        }

        companion object Parser {
            fun parse(input: List<String>) = Grid(
                input
                    .withIndex()
                    .transformTo(BitSet(input.size * input.first().length)) { bs, (i, s) ->
                        s.forEachIndexed { index, c -> bs[i * s.length + index] = c == '#' }
                    },
                input.size,
                input.first().length
            )
        }
    }
}

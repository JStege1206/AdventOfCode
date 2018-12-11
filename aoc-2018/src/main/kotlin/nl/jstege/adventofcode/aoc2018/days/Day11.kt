package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.applyIf
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.min
import kotlin.math.min

class Day11 : Day(title = "Chronal Charge") {
    companion object Configuration {
        private const val GRID_WIDTH = 300
        private const val GRID_HEIGHT = 300

        private const val FIRST_SQUARE_SIZE = 3
    }

    override fun first(input: Sequence<String>): Any = input.head.toInt()
        .solve(FIRST_SQUARE_SIZE, FIRST_SQUARE_SIZE)
        .let { (p, size) -> "${p.x - size + 1},${p.y - size + 1}" }

    override fun second(input: Sequence<String>): Any = input.head.toInt()
        .solve(1, min(GRID_WIDTH, GRID_HEIGHT))
        .let { (p, size) -> "${p.x - size + 1},${p.y - size + 1},$size" }

    private fun Int.solve(minSquareSize: Int, maxSquareSize: Int): Pair<Point, Int> {
        val grid = IntArray((GRID_HEIGHT + 1) * (GRID_WIDTH + 1))
        var max = Pair(Point.ZERO_ZERO, Int.MIN_VALUE)

        var maxValue = Int.MIN_VALUE

        for (y in 1..GRID_WIDTH) {
            for (x in 1..GRID_HEIGHT) {
                grid[x, y] = powerInCell(this, x, y) + grid[x, y - 1] + grid[x - 1, y] -
                        grid[x - 1, y - 1]

                for (size in minSquareSize..min(maxSquareSize, x, y)) {
                    grid.getSumOfSquare(x, y, size)
                        .applyIf({ this > maxValue }) {
                            maxValue = this
                            max = Pair(Point.of(x, y), size)
                        }
                }
            }
        }
        return max
    }

    private fun powerInCell(serialNumber: Int, x: Int, y: Int): Int = (x + 10).let { rackId ->
        (rackId * y + serialNumber) * rackId / 100 % 10 - 5
    }

    private fun toIndex(x: Int, y: Int) = y * (GRID_WIDTH + 1) + x

    private fun IntArray.getSumOfSquare(x: Int, y: Int, size: Int): Int =
        this[x, y] - this[x, y - size] - this[x - size, y] + this[x - size, y - size]

    private operator fun IntArray.get(x: Int, y: Int): Int =
        this[toIndex(x, y)]

    private operator fun IntArray.set(x: Int, y: Int, value: Int) {
        this[toIndex(x, y)] = value
    }
}

package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.sortTo
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day(title = "Probably a Fire Hazard") {
    private companion object Configuration {
        private const val GRID_COLS = 1000
        private const val GRID_ROWS = 1000
        
        private const val INPUT_PATTERN_STRING =
            """(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val OP_INDEX = 1
        private const val X1_INDEX = 2
        private const val Y1_INDEX = 3
        private const val X2_INDEX = 4
        private const val Y2_INDEX = 5

        private val OP_INDICES = intArrayOf(OP_INDEX, X1_INDEX, Y1_INDEX, X2_INDEX, Y2_INDEX)
    }

    override fun first(input: Sequence<String>): Any = input
        .parse()
        .transformTo(BooleanArray(GRID_COLS * GRID_ROWS)) { grid, (op, from, to) ->
            (from.y..to.y).forEach { y ->
                (from.x..to.x).forEach { x ->
                    grid[x, y] = op == "turn on" || op == "toggle" && !grid[x, y]
                }
            }
        }
        .count { it }

    override fun second(input: Sequence<String>): Any = input
        .parse()
        .transformTo(IntArray(GRID_ROWS * GRID_COLS)) { grid, (op, from, to) ->
            (from.y..to.y).forEach { y ->
                (from.x..to.x).forEach { x ->
                    grid[x, y] = Math.max(
                        0, grid[x, y] + when (op) {
                            "turn on" -> 1
                            "turn off" -> -1
                            else -> 2
                        }
                    )
                }
            }
        }
        .sum()

    private fun Sequence<String>.parse(): Sequence<Operation> = this
        .map { it.extractValues(INPUT_REGEX, *OP_INDICES) }
        .map { (op, sx1, sy1, sx2, sy2) ->
            val (x1, x2) = sx1.toInt() sortTo sx2.toInt()
            val (y1, y2) = sy1.toInt() sortTo sy2.toInt()
            Operation(op, Point.of(x1, y1), Point.of(x2, y2))
        }

    private operator fun BooleanArray.get(x: Int, y: Int): Boolean = this[y * GRID_COLS + x]

    private operator fun IntArray.get(x: Int, y: Int): Int = this[y * GRID_COLS + x]

    private operator fun BooleanArray.set(x: Int, y: Int, v: Boolean) {
        this[y * GRID_COLS + x] = v
    }

    private operator fun IntArray.set(x: Int, y: Int, v: Int) {
        this[y * GRID_COLS + x] = v
    }

    data class Operation(val operation: String, val from: Point, val to: Point)
}

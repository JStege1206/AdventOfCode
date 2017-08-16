package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6
import nl.jstege.adventofcode.aoccommon.utils.extensions.sortTo

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day() {
    val GRID_ROWS = 1000
    val GRID_COLS = 1000
    val INPUT_PATTERN = "(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()

    override fun first(input: Sequence<String>): Any = input
            .parse()
            .fold(BooleanArray(GRID_ROWS * GRID_COLS)) { grid, (op, from, to) ->
                (from.y..to.y).forEach { y ->
                    (from.x..to.x).forEach { x ->
                        grid[x, y] = op == "turn on" || op == "toggle" && !grid[x, y]
                    }
                }
                grid
            }
            .count { it }

    override fun second(input: Sequence<String>): Any = input
            .parse()
            .fold(IntArray(GRID_ROWS * GRID_COLS)) { grid, (op, from, to) ->
                (from.y..to.y).forEach { y ->
                    (from.x..to.x).forEach { x ->
                        grid[x, y] = Math.max(0, grid[x, y] + when (op) {
                            "turn on" -> 1
                            "turn off" -> -1
                            else -> 2
                        })
                    }
                }
                grid
            }
            .sum()

    private fun Sequence<String>.parse(): Sequence<Triple<String, Point, Point>> = this
            .map { INPUT_PATTERN.matchEntire(it)?.groupValues!! }
            .map { (_, op, sx1, sy1, sx2, sy2) ->
                val (x1, x2) = sx1.toInt() sortTo sx2.toInt()
                val (y1, y2) = sy1.toInt() sortTo sy2.toInt()
                Triple(op, Point.of(x1, y1), Point.of(x2, y2))
            }

    private operator fun BooleanArray.get(x: Int, y: Int): Boolean = this[y * GRID_COLS + x]
    private operator fun IntArray.get(x: Int, y: Int): Int = this[y * GRID_COLS + x]

    private operator fun BooleanArray.set(x: Int, y: Int, v: Boolean) {
        this[y * GRID_COLS + x] = v
    }

    private operator fun IntArray.set(x: Int, y: Int, v: Int) {
        this[y * GRID_COLS + x] = v
    }
}

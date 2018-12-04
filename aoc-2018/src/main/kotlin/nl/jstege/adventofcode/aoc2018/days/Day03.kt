package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

class Day03 : Day(title = "No Matter How You Slice It") {
    private companion object Configuration {
        private const val GRID_WIDTH = 1000
        private const val GRID_HEIGHT = 1000

        private const val INPUT_PATTERN_STRING = """#(\d+)\s@\s(\d+),(\d+):\s(\d+)x(\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val ID_INDEX = 1
        private const val X_INDEX = 2
        private const val Y_INDEX = 3
        private const val WIDTH_INDEX = 4
        private const val HEIGHT_INDEX = 5

        private val PARAM_INDICES = intArrayOf(
            ID_INDEX,
            X_INDEX,
            Y_INDEX,
            WIDTH_INDEX,
            HEIGHT_INDEX
        )
    }

    override fun first(input: Sequence<String>): Any =
        input
            .parse()
            .transformTo(Grid(GRID_WIDTH, GRID_HEIGHT), Grid::set)
            .count { it > 1 }

    override fun second(input: Sequence<String>): Any =
        Grid(GRID_WIDTH, GRID_HEIGHT).let { grid ->
            input
                .parse()
                .onEach(grid::set)
                .toList() //Make sure all claims have been processed.
                .first { claim -> grid[claim].all { it == 1 } }
                .id
        }

    private fun Sequence<String>.parse(): Sequence<Claim> = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .map { it.map(String::toInt) }
        .map { (id, x, y, w, h) -> Claim(id, x, y, x + w, y + h) }

    private data class Claim(
        val id: Int,
        val startX: Int,
        val startY: Int,
        val endX: Int,
        val endY: Int
    )

    private class Grid(val width: Int = GRID_WIDTH, height: Int = GRID_HEIGHT) : Iterable<Int> {
        private val grid = IntArray(width * height)

        fun set(claim: Claim) =
            claim.let { (_, startX, startY, endX, endY) ->
                for (y in startY until endY) {
                    for (x in startX until endX) {
                        grid[y * width + x]++
                    }
                }
            }

        operator fun get(claim: Claim): List<Int> =
            claim.let { (_, startX, startY, endX, endY) ->
                (startY until endY).flatMap { y ->
                    grid.copyOfRange(y * width + startX, y * width + endX).toList()
                }
            }

        override fun iterator(): Iterator<Int> = grid.iterator()
    }
}

package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformToIndexed

class Day18 : Day(title = "Settlers of The North Pole") {
    private companion object Configuration {
        private const val OPEN_GROUND = '.'
        private const val TREE = '|'
        private const val LUMBERYARD = '#'
        private const val FIRST_MINUTES = 10
        private const val SECOND_MINUTES = 1_000_000_000
    }

    override fun first(input: Sequence<String>): Any = input.solve(FIRST_MINUTES)

    override fun second(input: Sequence<String>): Any = input.solve(SECOND_MINUTES)

    private fun Sequence<String>.solve(minutes: Int) = this
        .parse()
        .step(minutes)
        .countScores()
        .let { (it[TREE] ?: 0) * (it[LUMBERYARD] ?: 0) }

    private fun Sequence<String>.parse(): Grid = this
        .toList()
        .let { g ->
            Grid(g.first().length, g.size, g.flatMap { it.toList() }.toCharArray())
        }

    class Grid(private val width: Int, private val height: Int, private val grid: CharArray) {
        fun step(limit: Int, seen: Map<String, Int> = mapOf(), step: Int = 0): Grid =
            when {
                step == limit -> this
                grid.joinToString("") in seen ->
                    (0 until (limit - step) % (step - seen[grid.joinToString("")]!!))
                        .fold(this) { g, _ -> g.nextGrid() }
                else -> nextGrid()
                    .step(limit, seen + (grid.joinToString("") to step), step + 1)
            }

        fun countScores(): Map<Char, Int> = grid.toList().groupingBy { it }.eachCount()

        private fun toIndex(x: Int, y: Int) = y * width + x
        private fun fromIndex(index: Int) = Point.of(index % width, index / width)

        private fun nextGrid(): Grid = Grid(width, height, grid
            .transformToIndexed(CharArray(width * height)) { index, newGrid, c ->
                newGrid[index] = c.nextState(index.neighborValues())
            }
        )

        private fun Int.neighborValues(): Map<Char, Int> =
            fromIndex(this)
                .neighbors8
                .filter { (x, y) -> x in (0 until width) && y in (0 until height) }
                .map { grid[toIndex(it.x, it.y)] }
                .groupingBy { it }
                .eachCount()
                .withDefault { 0 }

        private fun Char.nextState(neighbours: Map<Char, Int>): Char = when (this) {
            OPEN_GROUND -> if (neighbours.getValue(TREE) >= 3) TREE else OPEN_GROUND
            TREE -> if (neighbours.getValue(LUMBERYARD) >= 3) LUMBERYARD else TREE
            LUMBERYARD ->
                if (neighbours.getValue(TREE) >= 1 && neighbours.getValue(LUMBERYARD) >= 1)
                    LUMBERYARD
                else OPEN_GROUND
            else -> throw IllegalStateException()
        }
    }
}

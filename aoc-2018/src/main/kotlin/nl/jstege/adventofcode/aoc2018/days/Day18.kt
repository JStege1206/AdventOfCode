package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day

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
        .let { scores -> (scores[TREE] ?: 0) * (scores[LUMBERYARD] ?: 0) }

    private fun Sequence<String>.parse(): Grid = this
        .toList()
        .let { g -> Grid(g.first().length, g.size, g.joinToString("") { it }) }

    private tailrec fun Grid.step(
        limit: Int,
        seen: Map<String, Int> = mapOf(),
        step: Int = 0
    ): Grid = when {
        //Limit reached, return the current grid
        step == limit -> this
        //We've seen this grid before, this means that for every "step - seen[grid]" minutes 
        //we will iterate over the same grids. Figure out how many steps are left to get to
        //the limit and skip all iterations which are redundant.
        grid in seen -> (0 until (limit - step) % (step - seen[grid]!!))
            .fold(this) { g, _ -> g.nextGrid() }
        //Calculate next grid, update "seen" with the current grid
        else -> nextGrid().step(limit, seen + (grid to step), step + 1)
    }

    class Grid(private val width: Int, private val height: Int, val grid: String) {
        fun countScores(): Map<Char, Int> = grid.groupingBy { it }.eachCount()

        private fun toIndex(x: Int, y: Int) = y * width + x

        fun nextGrid(): Grid = Grid(
            width,
            height,
            //Iterate over the current grid and for every character calculate it's new value.
            grid.withIndex().joinToString("") { (index, c) ->
                c.nextState(index.neighborValues()).toString()
            }
        )

        private fun Int.neighborValues(): Map<Char, Int> =
            this
                .neighbors()
                .groupingBy { it }
                .eachCount()
                .withDefault { 0 }

        private fun Char.nextState(ns: Map<Char, Int>): Char = when (this) {
            OPEN_GROUND -> if (ns.getValue(TREE) >= 3) TREE else OPEN_GROUND
            TREE -> if (ns.getValue(LUMBERYARD) >= 3) LUMBERYARD else TREE
            LUMBERYARD ->
                if (ns.getValue(TREE) >= 1 && ns.getValue(LUMBERYARD) >= 1) LUMBERYARD
                else OPEN_GROUND
            else -> throw IllegalStateException()
        }

        private fun Int.neighbors(): List<Char> {
            val result = mutableListOf<Char>()
            val x = this % width
            val y = this / width

            if (y > 0) result += grid[toIndex(x, y - 1)]
            if (y + 1 < height) result += grid[toIndex(x, y + 1)]

            if (x > 0) {
                result += grid[toIndex(x - 1, y)]
                if (y > 0) result += grid[toIndex(x - 1, y - 1)]
                if (y + 1 < height) result += grid[toIndex(x - 1, y + 1)]
            }

            if (x + 1 < width) {
                result += grid[toIndex(x + 1, y)]
                if (y > 0) result += grid[toIndex(x + 1, y - 1)]
                if (y + 1 < height) result += grid[toIndex(x + 1, y + 1)]
            }
            return result
        }
    }
}

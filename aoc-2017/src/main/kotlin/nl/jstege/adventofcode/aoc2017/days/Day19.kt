package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day19 : Day() {
    override val title: String = "A Series of Tubes"

    private companion object Configuration {
        private const val EMPTY = ' '
        private const val CORNER = '+'
        private const val VERTICAL = '|'
    }

    override fun first(input: Sequence<String>): Any {
        return input.followPath().first
    }

    override fun second(input: Sequence<String>): Any {
        return input.followPath().second
    }

    private fun Sequence<String>.followPath() = this.map { it.toCharArray() }.toList()
            .let { maze ->
                var current = Point.of(maze.head.indexOf(VERTICAL), 0)
                var direction: Direction = Direction.SOUTH
                val letters = mutableListOf<Char>()
                var pathLength = 0
                while (maze[current] != EMPTY) {
                    pathLength++
                    if (maze[current] == CORNER) {
                        direction = Direction.values()
                                .filter { it != direction.opposite }
                                .first { maze[current.moveDirection(it)] != EMPTY }
                    }
                    if (maze[current].isLetter()) letters += maze[current]
                    current = current.moveDirection(direction)
                }

                letters.joinToString("") to pathLength

            }

    private operator fun List<CharArray>.get(p: Point) = when {
        p.x in (0 until this.head.size) && p.y in (0 until this.size) -> this[p.y][p.x]
        else -> EMPTY
    }
}

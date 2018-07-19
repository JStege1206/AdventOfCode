package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import kotlin.reflect.KProperty1


/**
 *
 * @author Jelle Stege
 */
class Day01 : Day(title = "No Time for a Taxicab") {
    override fun first(input: Sequence<String>): Any {
        var dir = Direction.NORTH
        return input.first().parse().fold(Point.ZERO_ZERO) { p, (turn, steps) ->
            dir = dir.let(turn.directionMod)
            p.moveDirection(dir, steps)
        }.manhattan(Point.ZERO_ZERO)
    }

    override fun second(input: Sequence<String>): Any {
        var dir = Direction.NORTH
        var coordinate = Point.ZERO_ZERO
        val visited = mutableSetOf(coordinate)

        input.first().parse().forEach { (turn, steps) ->
            dir = dir.let(turn.directionMod)

            val first = coordinate.moveDirection(dir)
            val last = coordinate.moveDirection(dir, steps)

            val xs = if (last.x < first.x) first.x downTo last.x else first.x..last.x
            val ys = if (last.y < first.y) first.y downTo last.y else first.y..last.y
            Point.of(xs, ys).forEach {
                coordinate = it
                if (coordinate in visited) return coordinate.manhattan(Point.ZERO_ZERO)
                visited += coordinate
            }
        }
        throw IllegalStateException("No answer found.")
    }

    private fun String.parse(): List<Instruction> = this.split(", ").map {
        Instruction(Turn.parse(it.substring(0, 1)), it.substring(1).toInt())
    }

    private data class Instruction(val turn: Turn, val steps: Int)

    private enum class Turn(val directionMod: KProperty1<Direction, Direction>) {
        LEFT(Direction::left), RIGHT(Direction::right);

        companion object {
            fun parse(v: String) = when (v) {
                "L" -> LEFT
                else -> RIGHT
            }
        }
    }
}

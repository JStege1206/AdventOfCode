package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point


/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override fun first(input: Sequence<String>): Any {
        var dir = Direction.NORTH
        val (x, y) = input.first().parse().fold(Point.ZERO_ZERO) { p, (turn, steps) ->
            dir = dir.turn(turn)
            when (dir) {
                Direction.NORTH, Direction.SOUTH -> p.addY(dir.modifier * steps)
                else -> p.addX(dir.modifier * steps)
            }
        }
        return Math.abs(x) + Math.abs(y)
    }

    override fun second(input: Sequence<String>): Any {
        var dir = Direction.NORTH
        var coords = Point.ZERO_ZERO
        val visitedPoints = mutableSetOf(coords)

        for ((turn, steps) in input.first().parse()) {
            dir = dir.turn(turn)

            var cur = if (dir.isYaxis) coords.y else coords.x
            val dest = cur + (dir.modifier * steps)

            while (cur != dest) {
                coords = if (dir.isYaxis) coords.addY(dir.modifier) else coords.addX(dir.modifier)

                if (coords in visitedPoints) {
                    return Math.abs(coords.x) + Math.abs(coords.y)
                }
                visitedPoints += coords
                cur = if (dir.isYaxis) coords.y else coords.x
            }
        }
        throw IllegalStateException("No answer found.")
    }

    private fun String.parse(): List<Instruction> = this.split(", ").map {
        Instruction(Turn.parse(it.substring(0, 1)), it.substring(1).toInt())
    }

    private data class Instruction(val turn: Turn, val steps: Int)

    private enum class Direction(val modifier: Int, val isYaxis: Boolean) {
        NORTH(1, true) {
            override fun turn(turn: Turn) = if (turn == Turn.LEFT) WEST else EAST
        },
        EAST(1, false) {
            override fun turn(turn: Turn) = if (turn == Turn.LEFT) NORTH else SOUTH
        },
        SOUTH(-1, true) {
            override fun turn(turn: Turn) = if (turn == Turn.LEFT) EAST else WEST
        },
        WEST(-1, false) {
            override fun turn(turn: Turn) = if (turn == Turn.LEFT) SOUTH else NORTH
        };

        abstract fun turn(turn: Turn): Direction
    }

    private enum class Turn(val key: String) {
        LEFT("L"), RIGHT("R");

        companion object {
            fun parse(v: String): Turn = when (v) {
                LEFT.key -> LEFT
                RIGHT.key -> RIGHT
                else -> throw IllegalArgumentException("No constant with specified name.")
            }
        }
    }
}
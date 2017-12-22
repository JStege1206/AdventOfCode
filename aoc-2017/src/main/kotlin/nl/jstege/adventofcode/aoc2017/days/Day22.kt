package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import kotlin.reflect.KProperty1

/**
 *
 * @author Jelle Stege
 */
class Day22 : Day() {
    private companion object Configuration {
        const val CLEAN_NODE = '.'
        const val INFECTED_NODE = '#'
        const val FIRST_ITERATIONS = 10000
        const val SECOND_ITERATIONS = 10000000

        val INITIAL_DIRECTION = Direction.NORTH
    }

    override fun first(input: Sequence<String>): Any {
        return input
                .parse()
                .work(FIRST_ITERATIONS) {
                    when (it) {
                        Status.CLEAN -> Status.INFECTED
                        Status.INFECTED -> Status.CLEAN
                        else -> throw IllegalStateException()
                    }
                }
    }

    override fun second(input: Sequence<String>): Any {
        return input
                .parse()
                .work(SECOND_ITERATIONS) {
                    when (it) {
                        Status.INFECTED -> Status.FLAGGED
                        Status.CLEAN -> Status.WEAKENED
                        Status.WEAKENED -> Status.INFECTED
                        Status.FLAGGED -> Status.CLEAN
                    }
                }
    }

    private fun Sequence<String>.parse() = this
            .foldIndexed(mapOf<Point, Status>()) { y, nodes, line ->
                nodes + line.mapIndexed { x, c -> Point.of(x, y) to Status.of(c) }.toMap()
            }
            .toMutableMap()

    private fun MutableMap<Point, Status>.work(bursts: Int, stateMod: (Status) -> Status): Int {
        var currentLocation = Point.of(
                this.keys.map { it.x }.max()!! / 2,
                this.keys.map { it.y }.max()!! / 2)
        var direction = INITIAL_DIRECTION
        
        return (0 until bursts)
                .count {
                    val oldState = this[currentLocation] ?: Status.CLEAN
                    val newState = stateMod(oldState)
                    this[currentLocation] = newState
                    direction = oldState.directionMod.get(direction)
                    currentLocation = currentLocation.moveDirection(direction)
                    newState == Status.INFECTED
                }
    }

    private enum class Status(val directionMod: KProperty1<Direction, Direction>) {
        INFECTED(Direction::right),
        CLEAN(Direction::left),
        WEAKENED(Direction::forward),
        FLAGGED(Direction::back);

        companion object {
            fun of(c: Char) = when (c) {
                CLEAN_NODE -> CLEAN
                INFECTED_NODE -> INFECTED
                else -> throw IllegalArgumentException()
            }
        }
    }
}

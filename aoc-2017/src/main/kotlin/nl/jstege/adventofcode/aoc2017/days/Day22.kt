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
        const val CLEAN_CHAR = '.'
        const val INFECTED_CHAR = '#'
        const val FIRST_ITERATIONS = 10000
        const val SECOND_ITERATIONS = 10000000
        val INITIAL_DIRECTION = Direction.NORTH
    }

    override val title: String = "Sporifica Virus"

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
            .foldIndexed(mutableMapOf<Point, Node>()) { y, nodes, line ->
                nodes += line
                        .mapIndexed { x, c -> Point.of(x, y) to c }
                        .map { (p, c) -> p to Node(p, nodes, Status.of(c)) }
                        .toMap()
                nodes
            }
            .toMutableMap()

    private fun MutableMap<Point, Node>.work(bursts: Int, stateMod: (Status) -> Status): Int {
        val currentLocation = Point.of(
                this.keys.map { it.x }.max()!! / 2,
                this.keys.map { it.y }.max()!! / 2)
        var node = this.getOrPut(currentLocation) { Node(currentLocation, this) }
        var direction = INITIAL_DIRECTION

        return (0 until bursts)
                .count {
                    direction = node.status.directionMod.get(direction)
                    val newState = stateMod(node.status)
                    node.status = newState
                    node = node.move(direction)
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
                CLEAN_CHAR -> CLEAN
                INFECTED_CHAR -> INFECTED
                else -> throw IllegalArgumentException()
            }
        }
    }

    private class Node(
            val coordinate: Point,
            val cache: MutableMap<Point, Node>,
            var status: Status = Status.CLEAN) {
        val left by lazy {
            val np = coordinate.decX()
            cache.getOrPut(np, { Node(np, cache) })
        }
        val right by lazy {
            val np = coordinate.incX()
            cache.getOrPut(np, { Node(np, cache) })
        }
        val up by lazy {
            val np = coordinate.decY()
            cache.getOrPut(np, { Node(np, cache) })
        }
        val down by lazy {
            val np = coordinate.incY()
            cache.getOrPut(np, { Node(np, cache) })
        }

        fun move(dir: Direction) = when (dir) {
            Direction.NORTH -> up
            Direction.EAST -> right
            Direction.SOUTH -> down
            Direction.WEST -> left
        }
    }
}

package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoc2017.days.Day22.Status.*
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import kotlin.reflect.KProperty1

/**
 *
 * @author Jelle Stege
 */
class Day22 : Day(title = "Sporifica Virus") {
    private companion object Configuration {
        const val CLEAN_CHAR = '.'
        const val INFECTED_CHAR = '#'
        const val FIRST_ITERATIONS = 10000
        const val SECOND_ITERATIONS = 10000000
        val INITIAL_DIRECTION = Direction.NORTH
    }

    override fun first(input: Sequence<String>): Any {
        return input
            .parse()
            .work(FIRST_ITERATIONS) {
                when (it) {
                    CLEAN -> INFECTED
                    INFECTED -> CLEAN
                    else -> throw IllegalStateException()
                }
            }
    }

    override fun second(input: Sequence<String>): Any {
        return input
            .parse()
            .work(SECOND_ITERATIONS) {
                when (it) {
                    INFECTED -> FLAGGED
                    CLEAN -> WEAKENED
                    WEAKENED -> INFECTED
                    FLAGGED -> CLEAN
                }
            }
    }

    private fun Sequence<String>.parse() = this
        .foldIndexed(mutableMapOf<Point, Node>()) { y, nodes, line ->
            nodes.apply {
                this += line
                    .mapIndexed { x, c -> Point.of(x, y) to c }
                    .map { (p, c) -> p to Node(p, nodes, Status.of(c)) }
                    .toMap()
            }
        }
        .toMutableMap()

    private fun MutableMap<Point, Node>.work(bursts: Int, stateModifier: (Status) -> Status): Int {
        val currentLocation = Point.of(
            this.keys.map { it.x }.max()!! / 2,
            this.keys.map { it.y }.max()!! / 2
        )
        var node = this.getOrPut(currentLocation) { Node(currentLocation, this) }
        var direction = INITIAL_DIRECTION

        return (0 until bursts)
            .asSequence()
            .map {
                direction = direction.let(node.status.directionMod)
                stateModifier(node.status).apply {
                    node.status = this
                    node = node.move(direction)
                }
            }
            .count { it == INFECTED }
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
        var status: Status = CLEAN
    ) {
        val left by lazy { coordinate.decX().let(::createNode) }
        val right by lazy { coordinate.incX().let(::createNode) }
        val up by lazy { coordinate.decY().let(::createNode) }
        val down by lazy { coordinate.incY().let(::createNode) }

        private fun createNode(it: Point): Node = cache.getOrPut(it) { Node(it, cache) }

        fun move(dir: Direction): Node = when (dir) {
            Direction.NORTH -> up
            Direction.EAST -> right
            Direction.SOUTH -> down
            Direction.WEST -> left
        }
    }
}

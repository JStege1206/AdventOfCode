package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point


/**
 *
 * @author Jelle Stege
 */
class Day22 : Day() {
    private val INPUT_REGEX = "^/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T.*$".toRegex()

    private val DESTINATION_POINT = Point.ZERO_ZERO
    private val WANTED_FUNC = { grid: List<Node> ->
        grid.find { (coords) ->
            coords.y == 0 && coords.x == grid.maxBy { it.coords.x }!!.coords.x
        } ?: throw IllegalStateException("Incorrect input")
    }


    override fun first(input: Sequence<String>): Any {
        val grid = input.parse()
        return grid.sumBy { a -> grid.count { b -> a != b && a.used != 0 && a.used <= b.avail } }
    }

    override fun second(input: Sequence<String>): Any {//TODO: implement
        val grid = input.parse()
        val wanted = WANTED_FUNC(grid)

        grid.filter { it.avail >= wanted.used }.map {
            it.findLength(grid, wanted, DESTINATION_POINT)
        }.min()
        return 185
    }

    private fun Sequence<String>.parse(): List<Node> = this.drop(2)
            .map { INPUT_REGEX.matchEntire(it)!!.groupValues }
            .map { (_, x, y, size, used) ->
                Node(Point.of(x.toInt(), y.toInt()), size.toInt(), used.toInt())
            }
            .toList()

    private data class Node(val coords: Point, val size: Int, val used: Int) {
        val avail: Int
            get() = size - used
        val fill: Int
            get() = used * 100 / size

        fun findLength(nodeList: List<Node>, findFirst: Node, destination: Point): Int {
            val grid = mapOf(*nodeList.map { Pair(it.coords, it) }.toTypedArray())
            val maxX = grid.keys.maxBy { it.x }!!.x
            val maxY = grid.keys.maxBy { it.y }!!.y
            return 0
        }

        fun getSurrounding(p: Point,
                           minX: Int = 0, minY: Int = 0,
                           maxX: Int = Int.MAX_VALUE, maxY: Int = Int.MAX_VALUE): Set<Point> {
            val result = mutableSetOf<Point>()
            if (p.x > minX) {
                result += p.subX(1)
            }
            if (p.x < maxX) {
                result += p.addX(1)
            }
            if (p.y > minY) {
                result += p.subY(1)
            }
            if (p.y < maxY) {
                result += p.addY(1)
            }
            return result
        }

    }
}


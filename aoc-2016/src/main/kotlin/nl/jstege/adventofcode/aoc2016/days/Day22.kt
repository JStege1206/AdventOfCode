package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point


/**
 *
 * @author Jelle Stege
 */
class Day22 : Day() {
    private val INPUT_REGEX = "^/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T.*$".toRegex()

    override fun first(input: Sequence<String>): Any {
        val grid = input.parse()
        return grid.sumBy { a -> grid.count { b -> a != b && a.used != 0 && a.used <= b.avail } }
    }

    override fun second(input: Sequence<String>): Any {//TODO: implement
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
    }
}


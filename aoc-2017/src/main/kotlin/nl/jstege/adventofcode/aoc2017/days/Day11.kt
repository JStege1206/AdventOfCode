package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

/**
 *
 * @author Jelle Stege
 */
class Day11 : Day(title = "Hex Ed") {
    override fun first(input: Sequence<String>): Any {
        return input.head
            .toUpperCase()
            .split(",")
            .asSequence()
            .map { Direction.valueOf(it).direction }
            .fold(Point.ZERO_ZERO, Point::plus)
            .let { (x, y) ->
                if (x.sign == y.sign) abs(x + y)
                else max(abs(x), abs(y))
            }
    }

    override fun second(input: Sequence<String>): Any {
        return input.head
            .toUpperCase()
            .split(",")
            .asSequence()
            .map { Direction.valueOf(it).direction }
            .scan(Point.ZERO_ZERO, Point::plus)
            .map { (x, y) ->
                if (x.sign == y.sign) abs(x + y)
                else max(abs(x), abs(y))
            }
            .max()!!
    }

    private enum class Direction(x: Int, y: Int) {
        N(0, -1), NE(1, -1), SE(1, 0), S(0, 1), SW(-1, 1), NW(-1, 0);

        val direction = Point.of(x, y)
    }
}

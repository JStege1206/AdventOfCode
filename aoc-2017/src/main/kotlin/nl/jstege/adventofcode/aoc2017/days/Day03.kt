package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.pow
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt

/**
 *
 * @author Jelle Stege
 */
class Day03 : Day() {
    private companion object Configuration {
        /**
         * Initial value for second assignment's scan operation.
         */
        private val INITIAL_VALUE = 1 to mapOf(Point.ZERO_ZERO to 1)
    }

    override fun first(input: Sequence<String>): Any {
        return input.head.toInt().spiralIndexToPoint().run { abs(x) + abs(y) }
    }

    override fun second(input: Sequence<String>): Any {
        //Start summing at 2 -> (1, 0) since the sequence is started with index 1 -> (0,0) having 1.
        return (2 until Int.MAX_VALUE).asSequence()
                .map { it.spiralIndexToPoint() }
                .scan(INITIAL_VALUE) { (_, values), point ->
                    val sum = point.neighbors8
                            .map { values[it] ?: 0 }
                            .sum()
                    sum to (values + (point to sum))
                }
                .first { (sum, _) -> sum > input.head.toInt() }
                .first
    }

    private fun Int.spiralIndexToPoint(): Point {
        fun calculateCoordinate(k: Int, m: Int, n: Int) = (k + m * m - n - (m % 2)) / 2 * (-1 pow m)
        val n = this - 1 //AoC spiral irritatingly starts at 1 instead of 0.
        val m = round(sqrt(n.toDouble())).toInt()
        val k = abs(m * m - n) - m
        return Point.of(calculateCoordinate(k, m, n), calculateCoordinate(-k, m, n))
    }
}

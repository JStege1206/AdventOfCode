package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven

/**
 *
 * @author Jelle Stege
 */
class Day03 : Day() {
    val dirs = mapOf(
            '^' to { p: Point -> p.addY(1) },
            'v' to { p: Point -> p.subY(1) },
            '<' to { p: Point -> p.subX(1) },
            '>' to { p: Point -> p.addX(1) }
    )

    override fun first(input: Sequence<String>): Any = input.first()
            .fold(setOf(Point.ZERO_ZERO) to Point.ZERO_ZERO, { (s, p), c ->
                val np: Point = dirs[c]!!(p)
                (s + np) to np
            }).first.size

    override fun second(input: Sequence<String>) = input.first()
            .foldIndexed(setOf(Point.ZERO_ZERO) to Pair(Point.ZERO_ZERO, Point.ZERO_ZERO),
                    { i, (s, p), c ->
                        val np = dirs[c]!!(if (i.isEven()) p.first else p.second)
                        (s + np) to (if (i.isEven()) np to p.second else p.first to np)
                    }).first.size
}
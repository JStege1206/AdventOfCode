package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan

/**
 *
 * @author Jelle Stege
 */
class Day03 : Day() {
    private companion object Configuration {
        private val DIRECTION_MODIFIERS = mapOf(
                '^' to Point::incY,
                'v' to Point::decY,
                '<' to Point::decX,
                '>' to Point::incX
        )
    }

    override suspend fun first(input: Sequence<String>): Any = input.head.asSequence()
            .scan(Point.ZERO_ZERO) { visited, c -> DIRECTION_MODIFIERS[c]!!(visited) }
            .toSet()
            .size

    override suspend fun second(input: Sequence<String>): Any = input.head.asSequence()
            .withIndex()
            .scan(Point.ZERO_ZERO to Point.ZERO_ZERO) { (s, r), (i, c) ->
                if (i.isEven()) DIRECTION_MODIFIERS[c]!!(s) to r
                else s to DIRECTION_MODIFIERS[c]!!(r)
            }
            .flatMap { (s, r) -> sequenceOf(s, r) }
            .toSet()
            .size
}

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
class Day03 : Day(title = "Perfectly Spherical Houses in a Vacuum") {
    private companion object Configuration {
        private val DIRECTION_MODIFIERS = mapOf(
            '^' to Point::incY,
            'v' to Point::decY,
            '<' to Point::decX,
            '>' to Point::incX
        )
    }

    override fun first(input: Sequence<String>): Any = input.head.asSequence()
        .scan(Point.ZERO_ZERO) { location, c -> DIRECTION_MODIFIERS[c]!!(location) }
        .toSet()
        .size

    override fun second(input: Sequence<String>): Any = input.head
        .asSequence()
        .withIndex()
        .scan(Point.ZERO_ZERO to Point.ZERO_ZERO) { (s, r), (i, c) ->
            if (i.isEven()) Pair(DIRECTION_MODIFIERS[c]!!(s), r)
            else Pair(s, DIRECTION_MODIFIERS[c]!!(r))
        }
        .flatMap { (s, r) -> sequenceOf(s, r) }
        .toSet()
        .size
    
}

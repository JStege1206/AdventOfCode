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
                '^' to Point::north,
                'v' to Point::south,
                '<' to Point::west,
                '>' to Point::east
        )
    }

    override val title: String = "Perfectly Spherical Houses in a Vacuum"
    
    override fun first(input: Sequence<String>): Any = input.head.asSequence()
            .scan(Point.ZERO_ZERO) { location, c -> DIRECTION_MODIFIERS[c]!!(location) }
            .toSet()
            .size

    override fun second(input: Sequence<String>): Any = input.head.asSequence()
            .withIndex()
            .scan(Point.ZERO_ZERO to Point.ZERO_ZERO) { (s, r), (i, c) ->
                if (i.isEven()) DIRECTION_MODIFIERS[c]!!(s) to r
                else s to DIRECTION_MODIFIERS[c]!!(r)
            }
            .flatMap { (s, r) -> sequenceOf(s, r) }
            .toSet()
            .size
}

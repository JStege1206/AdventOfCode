package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven

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

    override fun first(input: Sequence<String>): Any = input.head
            .fold(listOf(Point.ZERO_ZERO)) { visited, c ->
                visited + DIRECTION_MODIFIERS[c]!!(visited.last())
            }
            .toSet().size

    override fun second(input: Sequence<String>): Any = input.first()
            .foldIndexed(listOf(Point.ZERO_ZERO) to listOf(Point.ZERO_ZERO)) { i, (s, r), c ->
                if (i.isEven()) {
                    (s + DIRECTION_MODIFIERS[c]!!(s.last())) to r
                } else {
                    s to (r + DIRECTION_MODIFIERS[c]!!(r.last()))
                }
            }
            .let { (first, second) -> first + second }
            .toSet().size
}

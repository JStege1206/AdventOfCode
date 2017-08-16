package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.mid
import nl.jstege.adventofcode.aoccommon.utils.extensions.min

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day() {
    override fun first(input: Sequence<String>): Any = input
            .map { it.split('x').map { it.toInt() } }
            .sumBy { (l, w, h) ->
                2 * (w * l + w * h + h * l) + min(l * w, w * h, h * l)
            }

    override fun second(input: Sequence<String>): Any = input
            .map { it.split('x').map { it.toInt() } }
            .sumBy { (l, w, h) -> 2 * min(l, w, h) + 2 * mid(l, w, h) + l * w * h }
}
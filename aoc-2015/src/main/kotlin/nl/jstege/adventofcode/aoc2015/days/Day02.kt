package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.mid
import nl.jstege.adventofcode.aoccommon.utils.extensions.min

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day(title = "I Was Told There Would Be No Math") {
    override fun first(input: Sequence<String>): Any = input
        .map { it.split('x').map { it.toInt() } }
        .sumBy { (length, width, height) ->
            2 * (width * length + width * height + height * length) +
                    min(length * width, width * height, height * length)
        }

    override fun second(input: Sequence<String>): Any = input
        .map { it.split('x').map { it.toInt() } }
        .sumBy { (length, width, height) ->
            2 * (min(length, width, height) + mid(length, width, height)) + length * width * height
        }
}

package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.highestOneBit
import nl.jstege.adventofcode.aoccommon.utils.extensions.log
import nl.jstege.adventofcode.aoccommon.utils.extensions.pow

/**
 *
 * @author Jelle Stege
 */
class Day19 : Day() {
    override fun first(input: Sequence<String>): Any {
        val i = input.first().toInt()
        return 2 * (i - i.highestOneBit()) + 1
    }

    override fun second(input: Sequence<String>): Any {
        val i = input.first().toInt()
        val p = 3 pow log(i, 3)
        return i - p + Math.max(i - 2 * p, 0)
    }
}
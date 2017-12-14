package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.highestOneBit
import nl.jstege.adventofcode.aoccommon.utils.extensions.log
import nl.jstege.adventofcode.aoccommon.utils.extensions.pow
import kotlin.math.max

/**
 *
 * @author Jelle Stege
 */
class Day19 : Day() {
    override suspend fun first(input: Sequence<String>): Any {
        return input.first().toInt().let {
            2 * (it - it.highestOneBit()) + 1
        }
    }

    override suspend fun second(input: Sequence<String>): Any {
        return input.first().toInt().let { i ->
            (3 pow log(i, 3)).let { p ->
                i - p + max(i - 2 * p, 0)
            }
        }
    }
}

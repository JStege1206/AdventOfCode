package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.transpose
import nl.jstege.adventofcode.aoccommon.utils.extensions.chunked


/**
 *
 * @author Jelle Stege
 */
class Day03 : Day() {
    private val WHITESPACE_REGEX = "\\s+".toRegex()
    override fun first(input: Sequence<String>): Any = input
            .map { it.trim().split(WHITESPACE_REGEX).map(String::toInt) }
            .filter { isValid(it[0], it[1], it[2]) }.toList().size

    override fun second(input: Sequence<String>): Any = input
            .map { it.trim().split(WHITESPACE_REGEX).map(String::toInt) }.toList()
            .transpose().flatten().chunked(3)
            .filter { isValid(it[0], it[1], it[2]) }.size

    private fun isValid(x: Int, y: Int, z: Int): Boolean = (x + y > z) && (x + z > y) && (y + z > x)
}
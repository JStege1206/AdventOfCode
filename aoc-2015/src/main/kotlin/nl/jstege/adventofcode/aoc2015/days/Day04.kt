package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.isPrefixedWithZeroes
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day(title = "The Ideal Stucking Stuffer") {
    private companion object Configuration {
        private const val STARTING_ZEROES_FIRST = 5
        private const val STARTING_ZEROES_SECOND = 6
    }

    override fun first(input: Sequence<String>): Any = input.head
        .bruteforce(STARTING_ZEROES_FIRST)

    override fun second(input: Sequence<String>): Any = input.head
        .bruteforce(STARTING_ZEROES_SECOND)

    private fun String.bruteforce(zeroes: Int): Int = MessageDigest.getInstance("MD5")
        .let { md5 ->
            (0 until Int.MAX_VALUE)
                .asSequence()
                .map { md5.digest("$this$it".toByteArray()) }
                .withIndex()
                .first { (_, d) -> d.isPrefixedWithZeroes(zeroes) }.index
        }

}

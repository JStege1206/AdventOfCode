package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.prefixedWithZeroes
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    private companion object Configuration {
        private const val FIRST_ZEROES = 5
        private const val SECOND_ZEROES = 6
    }

    override val title: String = "The Ideal Stucking Stuffer"

    override fun first(input: Sequence<String>): Any = input.head
            .bruteforce(FIRST_ZEROES)

    override fun second(input: Sequence<String>): Any = input.head
            .bruteforce(SECOND_ZEROES)

    private fun String.bruteforce(zeroes: Int): Int = MessageDigest.getInstance("MD5")
            .let { md5 ->
                (0 until Int.MAX_VALUE)
                        .asSequence()
                        .map { md5.digest("$this$it".toByteArray()) }
                        .withIndex()
                        .first { (_, d) -> d.prefixedWithZeroes(zeroes) }.index
            }

}

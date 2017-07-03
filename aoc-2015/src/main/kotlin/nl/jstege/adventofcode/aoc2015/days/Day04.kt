package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.prefixedWithZeroes
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    val FIRST_ZEROES = 5
    val SECOND_ZEROES = 6

    override fun first(input: Sequence<String>): Any {
        val prefix = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        return (0 until Int.MAX_VALUE).asSequence()
                .map { md5.digest("$prefix$it".toByteArray()) to it }
                .first { it.first.prefixedWithZeroes(FIRST_ZEROES) }.second
    }

    override fun second(input: Sequence<String>): Any {
        val prefix = input.first()
        val md5 = MessageDigest.getInstance("MD5")

        return (0 until Int.MAX_VALUE).asSequence()
                .map { md5.digest("$prefix$it".toByteArray()) to it }
                .first { it.first.prefixedWithZeroes(SECOND_ZEROES) }.second
    }

}
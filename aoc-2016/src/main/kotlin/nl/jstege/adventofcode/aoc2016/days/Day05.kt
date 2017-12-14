package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd
import nl.jstege.adventofcode.aoccommon.utils.extensions.prefixedWithZeroes
import nl.jstege.adventofcode.aoccommon.utils.extensions.toHexChar
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day() {
    private companion object Configuration {
        private const val PASSWORD_LENGTH = 8
        private const val REQUIRE_ZEROES_PREFIX = 5
    }

    override suspend fun first(input: Sequence<String>): Any {
        val doorId = input.first()
        val md5 = MessageDigest.getInstance("MD5")

        return (0 until Int.MAX_VALUE).asSequence()
                .map {
                    md5.digest((doorId + it).toByteArray())
                }
                .filter {
                    it.prefixedWithZeroes(REQUIRE_ZEROES_PREFIX)
                }
                .map {
                    getPos(it, REQUIRE_ZEROES_PREFIX)
                }
                .take(PASSWORD_LENGTH)
                .map(Int::toHexChar).joinToString("")
    }

    override suspend fun second(input: Sequence<String>): Any {
        val doorId = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        val positionsFound = BooleanArray(PASSWORD_LENGTH) { false }

        return (0 until Int.MAX_VALUE).asSequence()
                .map {
                    md5.digest((doorId + it).toByteArray())
                }
                .filter {
                    it.prefixedWithZeroes(REQUIRE_ZEROES_PREFIX)
                }
                .map {
                    getPos(it, REQUIRE_ZEROES_PREFIX) to it
                }
                .filter { (i, _) ->
                    i <= PASSWORD_LENGTH - 1 && !positionsFound[i]
                }
                .onEach {
                    positionsFound[it.first] = true
                }
                .take(PASSWORD_LENGTH)
                .sortedBy { (i, _) -> i }
                .map { (_, digest) ->
                    getPos(digest, REQUIRE_ZEROES_PREFIX + 1).toHexChar()
                }.joinToString("")
    }

    private fun getPos(ar: ByteArray, pos: Int): Int =
            if (pos.isOdd()) ar[pos / 2].toUnsignedInt() and 0x0F
            else ar[pos / 2].toUnsignedInt() ushr 4
}

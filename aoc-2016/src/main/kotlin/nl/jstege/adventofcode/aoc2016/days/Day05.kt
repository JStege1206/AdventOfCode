package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd
import nl.jstege.adventofcode.aoccommon.utils.extensions.toHexChar
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day() {
    private val INVALID_CHAR = -1

    private val PASSWORD_LENGTH = 8
    private val REQUIRE_ZEROES_PREFIX = 5

    override fun first(input: Sequence<String>): Any {
        val doorId = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        val password = IntArray(PASSWORD_LENGTH) { INVALID_CHAR }

        var charsFound = 0
        var i = 0
        while (charsFound < PASSWORD_LENGTH) {
            val dig = md5.digest((doorId + i).toByteArray())

            if (checkZeroPrefix(dig)) {
                password[charsFound] = getPos(dig, REQUIRE_ZEROES_PREFIX)
                charsFound++
            }
            i++
        }
        return password.map(Int::toHexChar).joinToString("")
    }

    override fun second(input: Sequence<String>): Any {
        val prefix = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        val password = IntArray(PASSWORD_LENGTH) { INVALID_CHAR }

        var charsFound = 0
        var i = 0
        while (charsFound < PASSWORD_LENGTH) {
            val digest = md5.digest((prefix + i).toByteArray())

            if (checkZeroPrefix(digest)) {
                val p = getPos(digest, REQUIRE_ZEROES_PREFIX)
                if (p <= 7 && password[p] == INVALID_CHAR) {
                    password[p] = getPos(digest, REQUIRE_ZEROES_PREFIX + 1)
                    charsFound++
                }
            }
            i++
        }
        return password.map(Int::toHexChar).joinToString("")
    }

    private fun checkZeroPrefix(ar: ByteArray, zeroes: Int = REQUIRE_ZEROES_PREFIX): Boolean {
        val checks = zeroes / 2
        val zero = 0.toByte()
        if (ar.size < checks) return false
        (0 until checks)
                .filter { ar[it] != zero }
                .any { return false }
        return !(zeroes.isOdd() && (ar.size < checks + 1) || ((ar[checks].toInt() and 0xF0 != 0)))
    }

    private fun getPos(ar: ByteArray, pos: Int): Int =
            if (pos.isOdd()) ar[pos / 2].toUnsignedInt() and 0x0F
            else ar[pos / 2].toUnsignedInt() ushr 4
}
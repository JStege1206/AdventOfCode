package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.toHexString
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    private companion object Configuration {
        private const val KEYS_REQUIRED = 64
        private const val ADDITIONAL_STRETCHING_ITERATIONS = 2016

        private const val CORRESPONDING_HASH_WITHIN_ITERATIONS = 1000
    }

    override val title: String = "One-Time Pad"

    override fun first(input: Sequence<String>): Any {
        return input.head.bruteforce { md5, prefix, iteration ->
            md5.digest((prefix + iteration).toByteArray())
        }
    }

    override fun second(input: Sequence<String>): Any {
        return input.head.bruteforce { md5, prefix, iteration ->
            (0 until ADDITIONAL_STRETCHING_ITERATIONS)
                    .fold(md5.digest((prefix + iteration).toByteArray())) { previous, _ ->
                        md5.digest(previous.toHexString().toByteArray())
                    }
        }
    }

    private fun String.bruteforce(hasher: (MessageDigest, String, Int) -> ByteArray): Int {
        val md5 = MessageDigest.getInstance("MD5")
        val foundKeys = mutableListOf<Int>()
        val possibleKeys = mutableMapOf<Int, Char>()

        var iteration = 0
        while (foundKeys.size < KEYS_REQUIRED || possibleKeys.isNotEmpty()) {
            val digest = hasher(md5, this, iteration)

            if (foundKeys.size < KEYS_REQUIRED) {
                digest.contains3AndGet()?.let { possibleKeys[iteration] = it }
            }

            val iter = possibleKeys.iterator()
            while (iter.hasNext()) {
                val (key, value) = iter.next()
                if ((iteration - key >= CORRESPONDING_HASH_WITHIN_ITERATIONS)) {
                    iter.remove()
                    continue
                }
                if (key != iteration && digest.contains5Of(value)) {
                    foundKeys.add(key)
                    iter.remove()
                    continue
                }
            }
            iteration++
        }
        return foundKeys.sorted()[KEYS_REQUIRED - 1]
    }

    private fun ByteArray.contains3AndGet(): Char? {
        var i = 0
        while (i < this.size - 1) {
            val c = this[i].toInt() and 0x0F
            if ((c == (this[i].toInt() and 0xF0 ushr 4)
                    && c == (this[i + 1].toInt() and 0xF0 ushr 4))
                    || (c == (this[i + 1].toInt() and 0xF0 ushr 4)
                    && c == (this[i + 1].toInt() and 0x0F))) {
                return c.toChar()
            }
            i++
        }

        return null
    }

    private fun ByteArray.contains5Of(c: Char): Boolean {
        var i = 0
        while (i < this.size - 2) {
            val (c1, c2) = this[i].getChars()
            val (c3, c4) = this[i + 1].getChars()
            val (c5, c6) = this[i + 2].getChars()
            if (c2 == c && c3 == c && c4 == c && c5 == c && (c1 == c || c6 == c)) {
                return true
            }
            i++
        }

        return false
    }

    private fun Byte.getChars(): Pair<Char, Char> {
        return (this.toInt() and 0xF0 ushr 4).toChar() to (this.toInt() and 0x0F).toChar()
    }
}

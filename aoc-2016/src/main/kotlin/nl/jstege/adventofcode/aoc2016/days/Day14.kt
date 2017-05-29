package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.toHexString
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.security.MessageDigest

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    private val INVALID_CHAR = (-1).toChar()
    private val KEYS_REQUIRED = 64

    private val ADDITIONAL_STRETCHING_ITERATIONS = 2016
    private val CORRESPONDING_HASH_WITHIN_ITERATIONS = 1000

    override fun first(input: Sequence<String>): Any {
        val prefix = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        val foundKeys = hashSetOf<Int>()
        val possibleKeys = HashMap<Int, Char>()

        var iteration = 0
        while (foundKeys.size < KEYS_REQUIRED || possibleKeys.isNotEmpty()) {
            val digest = md5.digest((prefix + iteration).toByteArray())
            if (foundKeys.size < KEYS_REQUIRED) {
                val c = digest.contains3AndGet()
                if (c != INVALID_CHAR) {
                    possibleKeys[iteration] = c
                }
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

    override fun second(input: Sequence<String>): Any {
        val prefix = input.first()
        val md5 = MessageDigest.getInstance("MD5")
        val foundKeys = mutableListOf<Int>()
        val possibleKeys = HashMap<Int, Char>()

        var iteration = 0
        while (foundKeys.size < KEYS_REQUIRED || possibleKeys.isNotEmpty()) {
            var digest = md5.digest((prefix + iteration).toByteArray())

            (0 until ADDITIONAL_STRETCHING_ITERATIONS).forEach {
                digest = md5.digest(digest.toHexString().toByteArray())
            }

            if (foundKeys.size < KEYS_REQUIRED) {
                val c = digest.contains3AndGet()
                if (c != INVALID_CHAR) {
                    possibleKeys[iteration] = c
                }
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


    private fun ByteArray.contains3AndGet(): Char {
        var i = 0
        while (i < this.size - 1) {
            val c = this[i].toUnsignedInt() and 0x0F
            if ((c == (this[i].toUnsignedInt() and 0xF0 ushr 4)
                    && c == (this[i + 1].toUnsignedInt() and 0xF0 ushr 4))
                    || (c == (this[i + 1].toUnsignedInt() and 0xF0 ushr 4)
                    && c == (this[i + 1].toUnsignedInt() and 0x0F))) {
                return c.toChar()
            }
            i++
        }

        return INVALID_CHAR
    }

    private fun ByteArray.contains5Of(c: Char): Boolean {
        var i = 0
        while (i < this.size - 2) {
            val (c1, c2) = this[i].getChars()
            val (c3, c4) = this[i + 1].getChars()
            val (c5, c6) = this[i + 2].getChars()
            if ((c1 == c && c2 == c && c3 == c && c4 == c && c5 == c)
                    || (c2 == c && c3 == c && c4 == c && c5 == c && c6 == c)) {
                return true
            }
            i++
        }

        return false
    }

    private fun Byte.getChars(): Pair<Char, Char> {
        return Pair((this.toUnsignedInt() and 0xF0 ushr 4).toChar(),
                (this.toUnsignedInt() and 0x0F).toChar())
    }
}
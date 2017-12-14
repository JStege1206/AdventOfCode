package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day11 : Day() {
    private companion object Configuration {
        private val ILLEGAL_CHARS = setOf('i', 'o', 'l')
    }

    override suspend fun first(input: Sequence<String>): Any {
        val chars = input.first().toCharArray()
        while (!chars.isValid()) {
            chars.next()
        }
        return String(chars)
    }

    override suspend fun second(input: Sequence<String>): Any {
        val chars = input.first().toCharArray()
        var valid = 0
        while (valid < 2) {
            chars.next()
            if (chars.isValid()) {
                valid++
            }
        }
        return String(chars)
    }

    private fun CharArray.next() {
        var curPos = this.size - 1
        this[curPos] = this[curPos] + 1
        while (curPos >= 0 && this[curPos] > 'z') {
            this[curPos] = 'a'
            if (curPos > 0) {
                this[curPos - 1] = this[curPos - 1] + 1
            }
            curPos--
        }
    }

    private fun CharArray.isValid(): Boolean =
            this.hasStraight() && !this.hasIllegalChars() && this.hasTwoPairs()

    private fun CharArray.hasTwoPairs(): Boolean {
        var matches = 0
        var i = 0
        while (i < this.size - 1) {
            if (this[i] == this[i + 1]) {
                matches++
                i++
            }
            i++
        }
        return matches >= 2
    }

    private fun CharArray.hasIllegalChars() = this.any { it in ILLEGAL_CHARS }

    private fun CharArray.hasStraight(): Boolean {
        (0 until this.size - 2).forEach {
            val cur = this[it]
            if (cur + 1 == this[it + 1] && cur + 2 == this[it + 2]) {
                return true
            }
        }
        return false
    }
}

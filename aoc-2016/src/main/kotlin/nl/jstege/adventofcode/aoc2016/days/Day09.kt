package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day(title = "Explosives in Cyperspace") {
    private companion object Configuration {
        private const val DECOMPRESS_RECURSIVELY_FIRST = false
        private const val DECOMPRESS_RECURSIVELY_SECOND = true
    }

    override fun first(input: Sequence<String>): Any = input.first()
        .calcDecompressedLength(DECOMPRESS_RECURSIVELY_FIRST)

    override fun second(input: Sequence<String>): Any = input.first()
        .calcDecompressedLength(DECOMPRESS_RECURSIVELY_SECOND)


    private fun String.calcDecompressedLength(calcRec: Boolean): Long {
        val start = this.indexOf('(').apply {
            if (this == -1) return this@calcDecompressedLength.length.toLong()
        }

        val end = this.indexOf(')', start + 1)
        val (charAmt, repeatAmt) = this.substring(start + 1, end).split('x').map(String::toInt)

        val block = if (calcRec) {
            this.substring(end + 1, end + 1 + charAmt).calcDecompressedLength(calcRec)
        } else {
            charAmt.toLong()
        }
        return start + (repeatAmt * block) +
                this.substring(end + 1 + charAmt).calcDecompressedLength(calcRec)
    }
}

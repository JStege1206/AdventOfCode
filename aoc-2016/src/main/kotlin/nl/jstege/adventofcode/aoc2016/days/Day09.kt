package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day() {
    private companion object Configuration {
        val DECOMPRESS_RECURSIVELY_FIRST = false
        val DECOMPRESS_RECURSIVELY_SECOND = true
    }

    override suspend fun first(input: Sequence<String>): Any = input.first()
            .calcDecompressedLength(DECOMPRESS_RECURSIVELY_FIRST)

    override suspend fun second(input: Sequence<String>): Any = input.first()
            .calcDecompressedLength(DECOMPRESS_RECURSIVELY_SECOND)

    private fun String.calcDecompressedLength(calcRec: Boolean): Long {
        if (this.indexOf('(') == -1) return this.length.toLong()

        val start = this.indexOf('(')
        val end = this.indexOf(')', start + 1)
        val (charAmt, repeatAmt) = this.substring(start + 1, end).split('x').map(String::toInt)
        return this.substring(0, start).length + (repeatAmt * (
                if (calcRec) {
                    this.substring(end + 1, end + 1 + charAmt).calcDecompressedLength(calcRec)
                } else {
                    this.substring(end + 1, end + 1 + charAmt).length.toLong()
                })) +
                (this.substring(end + 1 + charAmt, this.length)).calcDecompressedLength(calcRec)

    }
}

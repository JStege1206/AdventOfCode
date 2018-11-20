package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd

/**
 *
 * @author Jelle Stege
 */
class Day16 : Day(title = "Dragon Checksum") {
    private companion object Configuration {
        private const val DISK_SIZE_FIRST = 272
        private const val DISK_SIZE_SECOND = 35651584
    }

    override fun first(input: Sequence<String>): Any = input.first()
        .fillDisk(DISK_SIZE_FIRST)
        .generateChecksum()

    override fun second(input: Sequence<String>): Any = input.first()
        .fillDisk(DISK_SIZE_SECOND)
        .generateChecksum()

    private fun String.fillDisk(size: Int) = fillDisk(StringBuilder(this), size)
    private fun String.generateChecksum(): String = generateChecksum(StringBuilder(this))

    private tailrec fun fillDisk(input: StringBuilder, size: Int): String =
        if (input.length >= size) input.substring(0, size)
        else {
            val copy = input.negate().reverse()
            fillDisk(input.append('0').append(copy), size)
        }


    private tailrec fun generateChecksum(input: StringBuilder = StringBuilder()): String =
        if (input.length.isOdd || input.isEmpty()) input.toString()
        else generateChecksum(
            (0 until input.length step 2)
                .asSequence()
                .map { if (input[it] == input[it + 1]) '1' else '0' }
                .fold(StringBuilder(), StringBuilder::append)

        )

    private

    fun StringBuilder.negate(): StringBuilder = this
        .map { if (it == '1') '0' else '1' }
        .fold(StringBuilder(), StringBuilder::append)
}

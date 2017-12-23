package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd

/**
 *
 * @author Jelle Stege
 */
class Day16 : Day() {
    private companion object Configuration {
        private const val DISK_SIZE_FIRST = 272
        private const val DISK_SIZE_SECOND = 35651584
    }

    override val title: String = "Dragon Checksum"

    override fun first(input: Sequence<String>): Any = input.first()
            .fillDisk(DISK_SIZE_FIRST)
            .generateChecksum()

    override fun second(input: Sequence<String>): Any = input.first()
            .fillDisk(DISK_SIZE_SECOND)
            .generateChecksum()

    private fun String.fillDisk(size: Int) = fillDisk(StringBuilder(this), size)
    private fun String.generateChecksum(): String = generateChecksum(StringBuilder(this))

    private tailrec fun fillDisk(input: StringBuilder, size: Int): String =
            when (input.length.compareTo(size)) {
                0, 1 -> StringBuilder(input.substring(0, size)).toString()
                else -> {
                    val copy = input.negate().reverse()
                    fillDisk(input.append('0').append(copy), size)
                }
            }


    private tailrec fun generateChecksum(input: StringBuilder = StringBuilder()): String =
            when (input.length.isOdd() || input.isEmpty()) {
                true -> input.toString()
                else -> generateChecksum(
                        (0 until input.length step 2).asSequence()
                                .map { if (input[it] == input[it + 1]) '1' else '0' }
                                .fold(StringBuilder(), StringBuilder::append)
                )
            }

    private fun StringBuilder.negate(): StringBuilder = (0 until this.length)
            .map { if (this[it] == '1') '0' else '1' }
            .fold(StringBuilder(), StringBuilder::append)

}

package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.bitCount
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day(title = "Two-Factor Authentication") {

    private companion object Configuration {
        private const val SCREEN_WIDTH = 50
        private const val SCREEN_HEIGHT = 6
        private const val INPUT_PATTERN_STRING =
            """rect (\d+)x(\d+)|rotate (row|column) [xy]=(\d+) by (\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val INPUT_INDEX = 0
        private const val X_INDEX = 1
        private const val Y_INDEX = 2
        private const val AXIS_INDEX = 3
        private const val RC_INDEX = 4
        private const val SHIFT_INDEX = 5

        private val PARAM_INDICES = intArrayOf(
            INPUT_INDEX,
            X_INDEX,
            Y_INDEX,
            AXIS_INDEX,
            RC_INDEX,
            SHIFT_INDEX
        )

        private const val ASCII_LETTER_WIDTH = 5
        private val ASCII_LETTERS = mapOf(
            0b011001001010010111101001010010 to 'A',
            0b111001001011100100101001011100 to 'B',
            0b011001001010000100001001001100 to 'C',
            0b111001001010010100101001011100 to 'D',
            0b111101000011100100001000011110 to 'E',
            0b111101000011100100001000010000 to 'F',
            0b011001001010000101101001001110 to 'G',
            0b100101001011110100101001010010 to 'H',
            0b011100010000100001000010001110 to 'I',
            0b001100001000010000101001001100 to 'J',
            0b100101010011000101001010010010 to 'K',
            0b100001000010000100001000011110 to 'L',
            //M missing
            //N missing
            0b011001001010010100101001001100 to 'O',
            0b111001001010010111001000010000 to 'P',
            //Q missing
            0b111001001010010111001010010010 to 'R',
            0b011101000010000011000001011100 to 'S',
            0b011100010000100001000010000100 to 'T',
            0b100101001010010100101001001100 to 'U',
            //V missing
            //W missing
            //X missing
            0b100011000101010001000010000100 to 'Y',
            0b111100001000100010001000011110 to 'Z'
        )
    }

    override fun first(input: Sequence<String>): Any = input
        .parseAndExecute()
        .map(Long::bitCount)
        .sum()

    override fun second(input: Sequence<String>): Any = input
        .parseAndExecute()
        .ocr()

    private fun Sequence<String>.parseAndExecute(): LongArray = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .transformTo(LongArray(SCREEN_HEIGHT)) { screen, (input, x, y, axis, rc, shift) ->
            when {
                input.startsWith("rect") -> screen.rect(x.toInt(), y.toInt())
                axis == "row" -> screen.rotateRow(rc.toInt(), shift.toInt())
                else -> screen.rotateColumn(rc.toInt(), shift.toInt())
            }
        }

    private fun LongArray.rect(endX: Int, endY: Int) {
        (((1L shl endX) - 1) shl (SCREEN_WIDTH - endX)).let { mask ->
            (0 until endY).forEach { this[it] = this[it] or mask }
        }
    }

    private fun LongArray.rotateRow(row: Int, shift: Int) {
        //To rotate the row, one can also swap the last n bits (the tail) with the first
        //size - n bits (the head). In which n is the amount of bits to shift, with the screen
        //size taken into account.
        //The tail is then: row & (1 << shift - 1)
        //The head is then: row ^ tail >>> shift, or simply row >>> shift
        //The result would then be: row = (tail << (size - shift)) + (head >> shift)
        //To make sure this result is not bigger than the screen size, the result is masked with
        //1 << size - 1, using an and-operation.
        val rightShift = shift % SCREEN_WIDTH
        val leftShift = SCREEN_WIDTH - rightShift
        this[row] = ((this[row] shl leftShift) + (this[row] ushr rightShift)) and
                ((1L shl SCREEN_WIDTH) - 1) //Make sure the result is not bigger than the screen
        //width
    }

    private fun LongArray.rotateColumn(col: Int, shift: Int) {
        val colIndex = (SCREEN_WIDTH - col - 1)
        val colUnsetMask = (1L shl colIndex).inv()

        val els = this.fold(0L) { n, it -> (n shl 1) + ((it ushr colIndex) and 1) }
        val downShift = shift % SCREEN_HEIGHT
        val upShift = SCREEN_HEIGHT - downShift
        val shiftedBits = ((els shl upShift) + (els ushr downShift)) and
                ((1L shl SCREEN_HEIGHT) - 1) //Make sure the result is not bigger than the screen
        // height
        (0 until this.size).forEach {
            this[it] = this[it] and colUnsetMask or //Unset specific bit
                    // Set specific bit
                    (((shiftedBits ushr (SCREEN_HEIGHT - it - 1)) and 1) shl colIndex)
        }
    }

    private fun LongArray.ocr(): String = ((1L shl ASCII_LETTER_WIDTH) - 1).let { mask ->
        ((SCREEN_WIDTH - ASCII_LETTER_WIDTH) downTo 0 step ASCII_LETTER_WIDTH)
            .mapNotNull {
                this.fold(0) { n, l -> (n shl ASCII_LETTER_WIDTH) + ((l ushr it) and mask).toInt() }
                    .let(ASCII_LETTERS::get)
            }
            .joinToString("")
    }
}

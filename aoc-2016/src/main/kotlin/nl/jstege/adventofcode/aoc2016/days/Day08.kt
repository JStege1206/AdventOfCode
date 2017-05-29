package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.bitCount

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day() {
    private val INPUT_REGEX = "rect (\\d+)x(\\d+)|rotate (row|column) [xy]=(\\d+) by (\\d+)"
            .toRegex()
    private val RECT_COMMAND_X = 1
    private val RECT_COMMAND_Y = 2
    private val ROT_COMMAND_AXIS = 3
    private val ROT_COMMAND_RC = 4
    private val ROT_COMMAND_SHIFT = 5

    private val SCREEN_WIDTH = 50
    private val SCREEN_HEIGHT = 6

    private val ASCII_LETTER_WIDTH = 5
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

    override fun first(input: Sequence<String>): Any = input
            .parseAndExecute()
            .map(Long::bitCount)
            .sum()

    override fun second(input: Sequence<String>): Any = input
            .parseAndExecute()
            .ocr()

    private fun Sequence<String>.parseAndExecute(): LongArray = this
            .map {
                INPUT_REGEX.matchEntire(it)?.groupValues ?: throw IllegalStateException("Invalid input")
            }
            .fold(LongArray(SCREEN_HEIGHT), { screen, it ->
                if (it[0].startsWith("rect")) {
                    screen.rect(it[RECT_COMMAND_X].toInt(), it[RECT_COMMAND_Y].toInt())
                } else if (it[ROT_COMMAND_AXIS] == "row") {
                    screen.rotateRow(it[ROT_COMMAND_RC].toInt(), it[ROT_COMMAND_SHIFT].toInt())
                } else {
                    screen.rotateColumn(it[ROT_COMMAND_RC].toInt(), it[ROT_COMMAND_SHIFT].toInt())
                }
                screen
            })

    private fun LongArray.rect(endX: Int, endY: Int) {
        val mask = ((1L shl endX) - 1) shl (SCREEN_WIDTH - endX)
        (0 until endY).forEach {
            this[it] = this[it] or mask
        }
    }

    private fun LongArray.rotateRow(row: Int, shift: Int) {
        val mask = (1L shl (shift % SCREEN_WIDTH)) - 1
        val tail = this[row] and mask
        val head = this[row] and mask.inv()
        this[row] = (tail shl (SCREEN_WIDTH - (shift % SCREEN_WIDTH))) +
                (head ushr (shift % SCREEN_WIDTH))
    }

    private fun LongArray.rotateColumn(col: Int, shift: Int) {
        val columnMask = 1L shl (SCREEN_WIDTH - col - 1)
        val shiftMask = (1L shl (shift % SCREEN_HEIGHT)) - 1
        val els = this.fold(0L, { n, it ->
            (n shl 1) + ((it and columnMask) ushr (SCREEN_WIDTH - col - 1))
        })
        val head = (els and shiftMask.inv()) ushr (shift % SCREEN_HEIGHT)
        val tail = (els and shiftMask) shl (SCREEN_HEIGHT - (shift % SCREEN_HEIGHT))
        val shiftedBits = tail + head
        this.forEachIndexed { i, _ ->
            this[SCREEN_HEIGHT - i - 1] = this[SCREEN_HEIGHT - i - 1] and
                    (columnMask.inv()) or (((shiftedBits ushr i) and 1) shl
                    (SCREEN_WIDTH - col - 1))
        }
    }

    private fun LongArray.ocr(): String {
        val mask = (1L shl ASCII_LETTER_WIDTH) - 1
        return ((SCREEN_WIDTH - ASCII_LETTER_WIDTH) downTo 0 step ASCII_LETTER_WIDTH)
                .fold(StringBuilder(), { s, it ->
                    s.append(ASCII_LETTERS[
                            this.fold(0, {
                                n, l ->
                                (n shl ASCII_LETTER_WIDTH) + ((l ushr it) and mask).toInt()
                            })] ?: throw IllegalStateException("Can not OCR number"))
                }).toString()
    }
}
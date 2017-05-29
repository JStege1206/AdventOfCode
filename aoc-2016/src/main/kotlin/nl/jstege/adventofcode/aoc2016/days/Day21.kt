package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven

/**
 *
 * @author Jelle Stege
 */
class Day21 : Day() {
    private val SWAP_OPERATION_INDEX = 1
    private val SWAP_V1_INDEX = 2
    private val SWAP_V2_INDEX = 5

    private val ROTATE_OP_INDEX = 1
    private val ROTATE_V1_INDEX = 2
    private val ROTATE_V2_INDEX = 6

    private val REVERSE_V1_INDEX = 2
    private val REVERSE_V2_INDEX = 4

    private val MOVE_V1_INDEX = 2
    private val MOVE_V2_INDEX = 5

    private val UNSCRAMBLED_INPUT = "abcdefgh".toCharArray()
    private val SCRAMBLED_INPUT = "fbgdceah".toCharArray()

    override fun first(input: Sequence<String>): Any {
         return String(input
                 .map { it.split(' ') }
                 .fold(UNSCRAMBLED_INPUT, { outputArray, it ->
                     when (it.first()) {
                         "swap" ->
                             if (it[SWAP_OPERATION_INDEX] == "position") {
                                 outputArray.swap(it[SWAP_V1_INDEX].toInt(), it[SWAP_V2_INDEX].toInt())
                             } else {
                                 outputArray.swap(it[SWAP_V1_INDEX][0], it[SWAP_V2_INDEX][0])
                             }
                         "rotate" ->
                             if (it[ROTATE_OP_INDEX] == "based") {
                                 outputArray.rotate(it[ROTATE_V2_INDEX][0], false)
                             } else {
                                 outputArray.rotate(
                                         if (it[ROTATE_OP_INDEX] == "left") {
                                             outputArray.size - it[ROTATE_V1_INDEX].toInt()
                                         } else {
                                             it[ROTATE_V1_INDEX].toInt()
                                         }
                                 )
                             }
                         "reverse" -> outputArray.reverse(it[REVERSE_V1_INDEX].toInt(),
                                 it[REVERSE_V2_INDEX].toInt())

                         "move" -> outputArray.move(it[MOVE_V1_INDEX].toInt(),
                                 it[MOVE_V2_INDEX].toInt())

                     }
                     outputArray
                 }))
    }

    override fun second(input: Sequence<String>): Any {
        return String(input.toList().asReversed()
                .map { it.split(' ') }
                .fold(SCRAMBLED_INPUT, { outputArray, it ->
                    when (it.first()) {
                        "swap" ->
                            if (it[SWAP_OPERATION_INDEX] == "position") {
                                outputArray.swap(it[SWAP_V2_INDEX].toInt(), it[SWAP_V1_INDEX].toInt())
                            } else {
                                outputArray.swap(it[SWAP_V2_INDEX][0], it[SWAP_V1_INDEX][0])
                            }
                        "rotate" ->
                            if (it[ROTATE_OP_INDEX] == "based") {
                                outputArray.rotate(it[ROTATE_V2_INDEX][0], true)
                            } else {
                                outputArray.rotate(
                                        if (it[ROTATE_OP_INDEX] == "right") {
                                            outputArray.size - it[ROTATE_V1_INDEX].toInt()
                                        } else {
                                            it[ROTATE_V1_INDEX].toInt()
                                        }
                                )
                            }
                        "reverse" -> outputArray.reverse(it[REVERSE_V1_INDEX].toInt(),
                                it[REVERSE_V2_INDEX].toInt())

                        "move" -> outputArray.move(it[MOVE_V2_INDEX].toInt(),
                                it[MOVE_V1_INDEX].toInt())
                    }
                    outputArray
                }))
    }



    private fun CharArray.swap(i: Int, j: Int) {
        val t = this[i]
        this[i] = this[j]
        this[j] = t
    }

    private fun CharArray.swap(x: Char, y: Char) {
        this.forEachIndexed { i, c ->
            if (c == y) {
                this[i] = x
            } else if (c == x) {
                this[i] = y
            }
        }
    }

    private fun CharArray.rotate(steps: Int) {
        val realSteps = steps % this.size
        val result = CharArray(this.size)
        System.arraycopy(this, 0, result, realSteps, this.size - realSteps)
        System.arraycopy(this, this.size - realSteps, result, 0, realSteps)
        System.arraycopy(result, 0, this, 0, result.size)
    }

    private fun CharArray.rotate(c: Char, reverse: Boolean) {
        val index = (0 until this.size).first { this[it] == c }
        val shift = if (reverse) {
            this.size - ((index +
                    if (index != 0 && index.isEven()) {
                        this.size
                    } else
                        0
                    ) / 2 + 1) % this.size
        } else {
            1 + index + if (index >= 4) 1 else 0
        }
        this.rotate(shift)
    }

    private fun CharArray.reverse(x: Int, y: Int) {
        (0..((y - x) / 2)).forEach {
            val c = this[x + it]
            this[x + it] = this[y - it]
            this[y - it] = c
        }
    }

    private fun CharArray.move(x: Int, y: Int) {
        val range: IntProgression
        val mod: Int
        if (x > y) {
            range = (x downTo y + 1)
            mod = -1
        } else {
            range = (x until y)
            mod = 1
        }
        val r = this[x]
        range.forEach { this[it] = this[it + 1 * mod] }
        this[y] = r
    }
}
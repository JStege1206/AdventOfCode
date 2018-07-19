package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day21 : Day(title = "Scrambled Letters and Hash") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = "(swap|rotate|reverse|move) " +
                "(position|letter|right|left|(based on position of letter))s? " +
                "([a-z0-9])(( [a-z]+)* ([a-z0-9]))?.*"
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private val UNSCRAMBLED_INPUT = "abcdefgh".toCharArray()
        private val SCRAMBLED_INPUT = "fbgdceah".toCharArray()
    }

    override fun first(input: Sequence<String>): Any = input
        .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
        .transformTo(UNSCRAMBLED_INPUT) { outputArray, (_, op, op2, _, v1, _, _, v2) ->
            when (op) {
                "swap" ->
                    if (op2 == "position") outputArray.swap(v1.toInt(), v2.toInt())
                    else outputArray.swap(v1[0], v2[0])
                "rotate" -> when (op2) {
                    "right" -> outputArray.rotate(v1.toInt())
                    "left" -> outputArray.rotate(outputArray.size - v1.toInt())
                    else -> outputArray.rotate(v1[0], false)
                }
                "reverse" -> outputArray.reverse(v1.toInt(), v2.toInt())
                "move" -> outputArray.move(v1.toInt(), v2.toInt())
            }
        }.joinToString("")

    override fun second(input: Sequence<String>): Any = input
        .toList().asReversed()
        .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
        .transformTo(SCRAMBLED_INPUT) { outputArray, (_, op, op2, _, v1, _, _, v2) ->
            when (op) {
                "swap" ->
                    if (op2 == "position") outputArray.swap(v2.toInt(), v1.toInt())
                    else outputArray.swap(v2[0], v1[0])
                "rotate" -> when (op2) {
                    "right" -> outputArray.rotate(outputArray.size - v1.toInt())
                    "left" -> outputArray.rotate(v1.toInt())
                    else -> outputArray.rotate(v1[0], true)
                }
                "reverse" -> outputArray.reverse(v1.toInt(), v2.toInt())
                "move" -> outputArray.move(v2.toInt(), v1.toInt())
            }
        }.joinToString("")


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
        val idx = this.withIndex().first { (_, t) -> t == c }.index
        val shift = if (reverse) {
            this.size - ((idx + if (idx != 0 && idx.isEven()) this.size else 0) / 2 + 1) % this.size
        } else {
            1 + idx + if (idx >= 4) 1 else 0
        }
        this.rotate(shift)
    }

    private fun CharArray.reverse(x: Int, y: Int) {
        (0..((y - x) / 2)).forEach {
            this.swap(x + it, y - it)
        }
    }

    private fun CharArray.move(x: Int, y: Int) {
        val (range, mod) = if (x > y) {
            (x downTo y + 1) to -1
        } else {
            (x until y) to 1
        }
        val r = this[x]
        range.forEach { this[it] = this[it + 1 * mod] }
        this[y] = r
    }
}

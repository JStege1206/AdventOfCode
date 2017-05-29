package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day() {
    override fun first(input: Sequence<String>) = input.count { it.isNiceFirst() }
    override fun second(input: Sequence<String>) = input.count { it.isNiceSecond() }

    fun String.isNiceFirst() =
            this.hasFowels(3) && this.containsDoubleLetters() && !containsIllegalPattern()

    private fun String.isNiceSecond(): Boolean = this.pairOccursTwice() && this.charRepeatsWrap()

    private fun String.hasFowels(amount: Int) =
            this.length - this.replace("[aeiou]".toRegex(), "").length >= amount


    private fun String.containsDoubleLetters() = (0 until this.length - 1)
            .any { this[it] == this[it + 1] }

    private fun String.containsIllegalPattern() = this.contains("ab|cd|pq|xy".toRegex())

    private fun String.pairOccursTwice() = (0 until this.length - 3).asSequence()
            .any { i ->
                ((i + 2) until (this.length - 1)).asSequence()
                        .filter { this[it] == this[i] && this[it + 1] == this[i + 1] }
                        .any()
            }

    private fun String.charRepeatsWrap(): Boolean = (0 until this.length - 2)
            .any { this[it] == this[it + 2] }

}
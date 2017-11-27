package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day() {
    private companion object Configuration {
        private val LIMITED_VOWELS = setOf('a', 'e', 'i', 'o', 'u')
    }

    override fun first(input: Sequence<String>): Any = input
            .count {
                it.hasVowels(3) && it.containsDoubleLetters() && !it.containsIllegalPattern()
            }

    override fun second(input: Sequence<String>): Any = input
            .count {
                it.pairOccursTwice() && it.charRepeatsWrap()
            }

    private fun String.hasVowels(amount: Int): Boolean = this
            .count { it in LIMITED_VOWELS } >= amount

    private fun String.containsDoubleLetters(): Boolean = (0 until this.length - 1)
            .any { this[it] == this[it + 1] }

    private fun String.containsIllegalPattern(): Boolean = this.contains("ab|cd|pq|xy".toRegex())

    private fun String.pairOccursTwice(): Boolean = (0 until this.length - 3).asSequence()
            .any { i ->
                ((i + 2) until (this.length - 1)).asSequence()
                        .filter { this[it] == this[i] && this[it + 1] == this[i + 1] }
                        .any()
            }

    private fun String.charRepeatsWrap(): Boolean = (0 until this.length - 2)
            .any { this[it] == this[it + 2] }

}

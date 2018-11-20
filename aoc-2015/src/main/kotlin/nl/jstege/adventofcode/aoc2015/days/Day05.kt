package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.containsAny

/**
 *
 * @author Jelle Stege
 */
class Day05 : Day(title = "Doesn't He Have Intern-Elves For This?") {
    private companion object Configuration {
        private val LIMITED_VOWELS = setOf('a', 'e', 'i', 'o', 'u')
        private val ILLEGAL_PATTERNS = setOf("ab", "cd", "pq", "xy")
    }

    override fun first(input: Sequence<String>): Any = input
        .count { it.hasVowels(3) && it.containsDoubleLetters() && !it.containsIllegalPatterns() }

    override fun second(input: Sequence<String>): Any = input
        .count { it.pairOccursTwice() && it.charRepeatsWrap() }

    private fun String.hasVowels(amount: Int): Boolean = this
        .count { it in LIMITED_VOWELS } >= amount

    private fun String.containsDoubleLetters(): Boolean = this
        .asSequence()
        .zipWithNext()
        .any { (a, b) -> a == b }

    private fun String.containsIllegalPatterns() = this.containsAny(ILLEGAL_PATTERNS)

    private fun String.pairOccursTwice(): Boolean = (0 until this.length - 3)
        .asSequence()
        .any { i ->
            ((i + 2) until (this.length - 1))
                .asSequence()
                .any { j -> this[i] == this[j] && this[i + 1] == this[j + 1] }
        }

    private fun String.charRepeatsWrap(): Boolean = (0 until this.length - 2)
        .any { this[it] == this[it + 2] }
}

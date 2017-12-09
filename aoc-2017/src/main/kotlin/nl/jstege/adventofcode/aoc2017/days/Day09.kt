package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day() {
    override fun first(input: Sequence<String>): Any {
        return input.head.cleanUp().first
    }

    override fun second(input: Sequence<String>): Any {
        return input.head.cleanUp().second
    }

    private fun String.cleanUp(): Pair<Int, Int> {
        tailrec fun cleanUp(
                index: Int, score: Int, nest: Int, garbageCnt: Int, state: State): Pair<Int, Int> {
            if (index >= this.length) return score to garbageCnt
            return when (state) {
                State.DEFAULT -> when (this[index]) {
                // Move to garbage state
                    '<' -> cleanUp(index + 1, score, nest, garbageCnt, State.GARBAGE)
                // Open new nesting level
                    '{' -> cleanUp(index + 1, score, nest + 1, garbageCnt, state)
                // Close nesting level
                    '}' -> cleanUp(index + 1, score + nest, nest - 1, garbageCnt, state)
                // Move over extraneous characters, not present in my input, but we need an
                // else clause --'
                    else -> cleanUp(index + 1, score, nest, garbageCnt, state)
                }
                State.GARBAGE -> when (this[index]) {
                // Move to cancel state
                    '!' -> cleanUp(index + 1, score, nest, garbageCnt, State.CANCEL)
                // Move to default state
                    '>' -> cleanUp(index + 1, score, nest, garbageCnt, State.DEFAULT)
                // Move over garbage character.
                    else -> cleanUp(index + 1, score, nest, garbageCnt + 1, state)
                }
            // Cancel character and move back to garbage
                State.CANCEL -> cleanUp(index + 1, score, nest, garbageCnt, State.GARBAGE)
            }
        }
        return cleanUp(0, 0, 0, 0, State.DEFAULT)
    }

    private enum class State { DEFAULT, GARBAGE, CANCEL }
}

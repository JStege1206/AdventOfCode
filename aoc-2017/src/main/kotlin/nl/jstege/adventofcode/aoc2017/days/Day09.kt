package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day() {
    override val title: String = "Stream Processing"

    override fun first(input: Sequence<String>): Any {
        return input.head.cleanUp().first
    }

    override fun second(input: Sequence<String>): Any {
        return input.head.cleanUp().second
    }

    private fun String.cleanUp(): Pair<Int, Int> {
        tailrec fun cleanUp(index: Int, score: Int, nest: Int, garbageCount: Int,
                            state: State): Pair<Int, Int> =
                if (index >= this.length)
                    score to garbageCount
                else when (state) {
                    State.DEFAULT -> when (this[index]) {
                        '<' -> cleanUp(index + 1, score, nest, garbageCount, State.GARBAGE)
                        '{' -> cleanUp(index + 1, score, nest + 1, garbageCount, state)
                        '}' -> cleanUp(index + 1, score + nest, nest - 1, garbageCount, state)
                        else -> cleanUp(index + 1, score, nest, garbageCount, state)
                    }
                    State.GARBAGE -> when (this[index]) {
                        '!' -> cleanUp(index + 1, score, nest, garbageCount, State.CANCEL)
                        '>' -> cleanUp(index + 1, score, nest, garbageCount, State.DEFAULT)
                        else -> cleanUp(index + 1, score, nest, garbageCount + 1, state)
                    }
                    State.CANCEL -> cleanUp(index + 1, score, nest, garbageCount, State.GARBAGE)
                }
        return cleanUp(0, 0, 0, 0, State.DEFAULT)
    }

    private enum class State { DEFAULT, GARBAGE, CANCEL }
}

package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoc2017.days.Day09.State.*
import nl.jstege.adventofcode.aoc2017.days.Day09.Symbol.*
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.map

/**
 *
 * @author Jelle Stege
 */
class Day09 : Day(title = "Stream Processing") {

    override fun first(input: Sequence<String>): Any = input.head.parse().cleanUp().first

    override fun second(input: Sequence<String>): Any = input.head.parse().cleanUp().second

    private fun String.parse() = this.iterator().map(Symbol.Companion::parse)

    private fun Iterator<Symbol>.cleanUp(): Pair<Int, Int> {
        tailrec fun cleanUp(
            score: Int,
            nest: Int,
            garbageCount: Int,
            state: State
        ): Pair<Int, Int> =
            if (!hasNext()) Pair(score, garbageCount)
            else when (state) {
                DEFAULT -> when (next()) {
                    GARBAGE_OPEN -> cleanUp(score, nest, garbageCount, GARBAGE)
                    SCOPE_OPEN -> cleanUp(score, nest + 1, garbageCount, state)
                    SCOPE_CLOSE -> cleanUp(score + nest, nest - 1, garbageCount, state)
                    IGNORE, GARBAGE_CLOSE, MISCELLANEOUS -> cleanUp(
                        score, nest, garbageCount, state
                    )
                }
                GARBAGE -> when (next()) {
                    IGNORE -> cleanUp(score, nest, garbageCount, CANCEL)
                    GARBAGE_CLOSE -> cleanUp(score, nest, garbageCount, DEFAULT)
                    GARBAGE_OPEN, SCOPE_OPEN, SCOPE_CLOSE, MISCELLANEOUS -> cleanUp(
                        score, nest, garbageCount + 1, state
                    )
                }
                CANCEL -> {
                    next()
                    cleanUp(score, nest, garbageCount, GARBAGE)
                }
            }
        return cleanUp(0, 0, 0, DEFAULT)
    }

    private enum class State { DEFAULT, GARBAGE, CANCEL }

    private enum class Symbol(val symbol: Char? = null) {
        GARBAGE_OPEN('<'),
        GARBAGE_CLOSE('>'),
        SCOPE_OPEN('{'),
        SCOPE_CLOSE('}'),
        IGNORE('!'),
        MISCELLANEOUS;

        companion object {
            @JvmStatic
            fun parse(symbol: Char) = when (symbol) {
                GARBAGE_OPEN.symbol -> GARBAGE_OPEN
                GARBAGE_CLOSE.symbol -> GARBAGE_CLOSE
                SCOPE_OPEN.symbol -> SCOPE_OPEN
                SCOPE_CLOSE.symbol -> SCOPE_CLOSE
                IGNORE.symbol -> IGNORE
                else -> MISCELLANEOUS
            }
        }
    }
}

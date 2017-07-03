package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.toHexChar

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day() {
    private val INVALID = -1
    private val FIRST_STARTING_INDEX = Point.of(2, 2)
    private val SECOND_STARTING_INDEX = Point.of(1, 3)
    private val FIRST_KEYPAD = arrayOf(
            arrayOf(INVALID, INVALID, INVALID, INVALID, INVALID),
            arrayOf(INVALID, 1,       2,       3,       INVALID),
            arrayOf(INVALID, 4,       5,       6,       INVALID),
            arrayOf(INVALID, 7,       8,       9,       INVALID),
            arrayOf(INVALID, INVALID, INVALID, INVALID, INVALID)
    )
    private val SECOND_KEYPAD = arrayOf(
            arrayOf(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID),
            arrayOf(INVALID, INVALID, INVALID, 1,       INVALID, INVALID, INVALID),
            arrayOf(INVALID, INVALID, 2,       3,       4,       INVALID, INVALID),
            arrayOf(INVALID, 5,       6,       7,       8,       9,       INVALID),
            arrayOf(INVALID, INVALID, 10,      11,      12,      INVALID, INVALID),
            arrayOf(INVALID, INVALID, INVALID, 13,      INVALID, INVALID, INVALID),
            arrayOf(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID)
    )

    override fun first(input: Sequence<String>): Any =
            input.iterate(FIRST_KEYPAD, FIRST_STARTING_INDEX)
                    .joinToString("")


    override fun second(input: Sequence<String>): Any =
            input.iterate(SECOND_KEYPAD, SECOND_STARTING_INDEX)
                    .map { it.toHexChar().toUpperCase() }
                    .joinToString("")


    private fun Sequence<String>.iterate(keypad: Array<Array<Int>>,
                        startingPosition: Point) =
            this.fold(Pair(startingPosition, listOf<Int>()), { (pos, code), line ->
                val newPos = line.fold(pos, { p, it ->
                    when (it) {
                        'L' -> if (keypad[p.y][p.x - 1] != INVALID) p.subX(1) else p
                        'R' -> if (keypad[p.y][p.x + 1] != INVALID) p.addX(1) else p
                        'U' -> if (keypad[p.y - 1][p.x] != INVALID) p.subY(1) else p
                        'D' -> if (keypad[p.y + 1][p.x] != INVALID) p.addY(1) else p
                        else -> throw IllegalStateException("Invalid input")
                    }
                })
                Pair(newPos, code + keypad[newPos.y][newPos.x])
            }).second
}
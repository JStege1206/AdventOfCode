package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import java.util.*

class Day20 : Day(title = "A Regular Map") {
    companion object Configuration {
        private val DIRECTIONS = mapOf(
            'N' to Direction.NORTH,
            'E' to Direction.EAST,
            'S' to Direction.SOUTH,
            'W' to Direction.WEST
        )
    }

    override fun first(input: Sequence<String>): Any =
        input.head.solve().values.max() ?: 0

    override fun second(input: Sequence<String>): Any =
        input.head.solve().count { (_, v) -> v >= 1000 }

    private fun String.solve(): Map<Point, Int> = mutableMapOf(Point.ZERO_ZERO to 0).apply {
        fold(ArrayDeque<Point>() to Point.ZERO_ZERO) { (remainingPaths, currentLocation), c ->
            remainingPaths to when (c) {
                //On entering branched directions, add the currentLocation to remainingPaths, 
                //this will ensure that we can continue on this point to follow a different 
                //path.
                '(' -> currentLocation.also { remainingPaths.addFirst(it) }
                //On closing a branch we remove the last position we were on before entering
                //the branch and just go on.
                ')' -> remainingPaths.removeFirst()
                //As soon as we encounter a new branch (which is always preceeded by |), get the
                //last known location and use it to follow the directions.
                '|' -> remainingPaths.peekFirst()
                //Regular NESW directions, just follow it and update the distance if needed.
                in "NESW" -> currentLocation.moveDirection(DIRECTIONS[c]!!).also { newLocation ->
                    this.merge(newLocation, this[currentLocation]!! + 1) { newLength, prevLength ->
                        if (newLength > prevLength - 1) prevLength else newLength
                    }
                }
                //Non-necessary characters, e.g. ^ and $
                else -> currentLocation
            }
        }
    }
}

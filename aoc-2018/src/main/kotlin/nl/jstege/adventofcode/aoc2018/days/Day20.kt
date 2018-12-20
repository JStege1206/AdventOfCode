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
        input.head.iterator().determineRoutes().values.max() ?: 0

    override fun second(input: Sequence<String>): Any =
        input.head.iterator().determineRoutes().count { (_, v) -> v >= 1000 }

    private tailrec fun CharIterator.determineRoutes(
        crossroads: ArrayDeque<Point> = ArrayDeque(),
        currentLocation: Point = Point.ZERO_ZERO,
        distances: MutableMap<Point, Int> = mutableMapOf(Point.ZERO_ZERO to 0)
    ): Map<Point, Int> =
        if (!hasNext()) distances
        else when (val c = next()) {
            //On entering branched directions, add the currentLocation to crossroads, 
            //this will ensure that we can continue on this point to follow a different 
            //path.
            '(' -> determineRoutes(
                crossroads.apply { addFirst(currentLocation) },
                currentLocation,
                distances
            )

            //On closing a branch we remove the last crossroad we were on before entering
            //the branch and just go on.
            ')' -> determineRoutes(crossroads, crossroads.removeFirst(), distances)

            //As soon as we encounter a new branch, get the
            //last known crossroad and use it to follow the directions.
            '|' -> determineRoutes(crossroads, crossroads.peekFirst(), distances)

            //Regular NESW directions, just follow it and update the distance if needed.
            in "NESW" -> {
                val newLocation = currentLocation.moveDirection(DIRECTIONS[c]!!)
                determineRoutes(crossroads, newLocation, distances.apply {
                    merge(newLocation, this[currentLocation]!! + 1) { newLength, prevLength ->
                        if (newLength > prevLength - 1) prevLength else newLength
                    }
                })
            }

            //Non-necessary characters, e.g. ^ and $
            else -> determineRoutes(crossroads, currentLocation, distances)
        }
}

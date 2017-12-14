package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.bitCount
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd

/**
 *
 * @author Jelle Stege
 */
class Day13 : Day() {
    private companion object Configuration {
        private val SRC_POS = Point.ONE_ONE
        private val DEST_POS = Point.of(31, 39)
        private val MAX_STEPS = 50
    }

    override suspend fun first(input: Sequence<String>): Any =
            findPathLength(SRC_POS, DEST_POS, input.first().toInt())

    override suspend fun second(input: Sequence<String>): Any =
            getUniqueCoords(SRC_POS, MAX_STEPS, input.first().toInt()).size


    private fun isWall(p: Point, add: Int): Boolean = isWall(p.x, p.y, add)

    private fun isWall(x: Int, y: Int, add: Int): Boolean =
            ((x * x + 3 * x + 2 * x * y + y + y * y) + add).bitCount().isOdd()

    private fun findPathLength(start: Point, dest: Point, wallModifier: Int): Int {
        val toVisit = hashSetOf(start)
        val visited = hashSetOf<Point>()

        var pathLength = 0
        while (dest !in toVisit && toVisit.isNotEmpty()) {
            nextPositions(toVisit, visited, wallModifier)
            pathLength++
        }
        return pathLength
    }

    private fun getUniqueCoords(start: Point, maxSteps: Int, wallModifier: Int): Set<Point> {
        val toVisit = hashSetOf(start)
        val visited = hashSetOf<Point>()

        var pathLength = 0
        while (pathLength <= maxSteps && toVisit.isNotEmpty()) {
            nextPositions(toVisit, visited, wallModifier)
            pathLength++
        }
        return visited
    }

    private fun nextPositions(queue: HashSet<Point>, visited: HashSet<Point>, wallModifier: Int) {
        visited += queue
        queue += queue.flatMap {
            it.neighbors4
                    .filter {
                        it.y >= 0 && it.x >= 0 && !isWall(it, wallModifier) && it !in visited
                    }
        }
        queue.removeAll(visited)
    }
}

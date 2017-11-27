package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.toUnsignedInt
import java.security.MessageDigest
import java.util.*
import kotlin.reflect.KFunction2

/**
 *
 * @author Jelle Stege
 */
class Day17 : Day() {
    private companion object Configuration {
        private const val FIRST = true
        private const val SECOND = !FIRST

        private const val MIN_X_COORD = 0
        private const val MIN_Y_COORD = 0
        private const val MAX_X_COORD = 3
        private const val MAX_Y_COORD = 3

        private const val MIN_CAPACITY = 300

        private val STARTING_POINT = Point.of(MIN_X_COORD, MIN_Y_COORD)
        private val DESTINATION_POINT = Point.of(MAX_X_COORD, MAX_Y_COORD)
        
        private val ASSIGNMENT_MOD = mapOf(
                FIRST to Pair(ArrayDeque<Pair<String, Point>>::add,
                        { p1: String, p2: String -> p1.isEmpty() || p1.length > p2.length }),
                SECOND to Pair(ArrayDeque<Pair<String, Point>>::addFirst,
                        { p1: String, p2: String -> p1.length < p2.length })
        )
    }

    override fun first(input: Sequence<String>): Any =
            findPath(input.first(), STARTING_POINT, DESTINATION_POINT, ASSIGNMENT_MOD[FIRST]!!)

    override fun second(input: Sequence<String>): Any =
            findPath(input.first(), STARTING_POINT, DESTINATION_POINT, ASSIGNMENT_MOD[SECOND]!!)
                    .length


    private fun findPath(input: String, source: Point, destination: Point,
                         modifier: Pair<KFunction2<ArrayDeque<Pair<String, Point>>,
                                 @ParameterName(name = "element") Pair<String, Point>, Any>,
                                 (String, String) -> Boolean>): String {
        val (adder, neededCondition) = modifier
        val md = MessageDigest.getInstance("MD5")
        val deque = ArrayDeque<Pair<String, Point>>(MIN_CAPACITY)
        adder(deque, Pair("", source))
        var foundPath = ""
        while (deque.isNotEmpty()) {
            val (path, currentPosition) = deque.removeFirst()
            if (currentPosition == destination) {
                if (neededCondition(foundPath, path)) {
                    foundPath = path
                }
                continue
            }
            val (first, second) = md.digest((input + path).toByteArray())
            if (first.toUnsignedInt() ushr 4 > 10 && currentPosition.y != MIN_Y_COORD) {
                adder(deque, Pair(path + "U", currentPosition.subY(1)))
            }
            if (first.toUnsignedInt() and 0x0F > 10 && currentPosition.y != MAX_Y_COORD) {
                adder(deque, Pair(path + "D", currentPosition.addY(1)))
            }
            if (second.toUnsignedInt() ushr 4 > 10 && currentPosition.x != MIN_X_COORD) {
                adder(deque, Pair(path + "L", currentPosition.subX(1)))
            }
            if (second.toUnsignedInt() and 0x0F > 10 && currentPosition.x != MAX_X_COORD) {
                adder(deque, Pair(path + "R", currentPosition.addX(1)))
            }
        }
        return foundPath
    }
}

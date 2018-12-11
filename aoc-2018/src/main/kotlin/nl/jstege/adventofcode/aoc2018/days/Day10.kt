package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import kotlin.math.max
import kotlin.math.min

class Day10 : Day(title = "The Stars Align") {
    private companion object Configuration {
        private val INPUT_REGEX =
            """position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>""".toRegex()

        private val PARAM_INDICES = intArrayOf(1, 2, 3, 4)

        private const val LETTER_WIDTH = 6
        private const val LETTER_HEIGHT = 10
        private const val LETTER_SPACING = 2

        private val LETTER_MAPPING = mapOf(
            0b001100010010100001100001100001111111100001100001100001100001 to "A",
            0b111110100001100001100001111110100001100001100001100001111110 to "B",
            0b011110100001100000100000100000100000100000100000100001011110 to "C",
            0b111111100000100000100000111110100000100000100000100000111111 to "E",
            0b111111100000100000100000111110100000100000100000100000100000 to "F",
            0b011110100001100000100000100000100111100001100001100011011101 to "G",
            0b100001100001100001100001111111100001100001100001100001100001 to "H",
            0b000111000010000010000010000010000010000010100010100010011100 to "J",
            0b100001100010100100101000110000110000101000100100100010100001 to "K",
            0b100000100000100000100000100000100000100000100000100000111111 to "L",
            0b100001110001110001101001101001100101100101100011100011100001 to "N",
            0b111110100001100001100001111110100000100000100000100000100000 to "P",
            0b111110100001100001100001111110100100100010100010100001100001 to "R",
            0b100001100001010010010010001100001100010010010010100001100001 to "X",
            0b111111000001000001000010000100001000010000100000100000111111 to "Z"
        )
    }

    override fun first(input: Sequence<String>): Any {
        return input
            .parse()
            .run {
                pointsAtTime(searchOptimal())
                    .let { (result, min, max) ->
                        result.parseOcr(min, max)
                    }
            }
    }

    override fun second(input: Sequence<String>): Any {
        return input
            .parse()
            .searchOptimal()
    }

    private fun List<Vector>.boxSurfaceAtTime(t: Int): Long =
        this.pointsAtTime(t).let { (_, min, max) -> (max.x.toLong() - min.x) * (max.y - min.y) }

    private tailrec fun List<Vector>.searchOptimal(
        left: Int = 0,
        right: Int = Int.MAX_VALUE / 1000
    ): Int {
        if (left >= right) return left

        val leftThird = (2 * left + right) / 3
        val rightThird = (left + 2 * right) / 3

        return if (boxSurfaceAtTime(leftThird) > boxSurfaceAtTime(rightThird)) {
            searchOptimal(leftThird + 1, right)
        } else {
            searchOptimal(left, rightThird)
        }
    }

    private fun List<Vector>.pointsAtTime(steps: Int = 1): Triple<List<Vector>, Point, Point> {
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        return this.map { vector ->
            vector.copy(point = vector.point + (vector.velocity * steps)).also { (newPoint, _) ->
                minX = min(newPoint.x, minX)
                minY = min(newPoint.y, minY)
                maxX = max(newPoint.x, maxX)
                maxY = max(newPoint.y, maxY)
            }
        }.let { newVectors ->
            Triple(newVectors, Point.of(minX, minY), Point.of(maxX, maxY))
        }
    }

    private fun List<Vector>.parseOcr(min: Point, max: Point): String =
        this.map { it.point }
            .toSet()
            .let { points ->
                (min.x..max.x step LETTER_WIDTH + LETTER_SPACING).joinToString("") { start ->
                    (min.y until min.y + LETTER_HEIGHT).fold(0L) { letter, y ->
                        (start until start + LETTER_WIDTH).fold(letter) { letterRow, x ->
                            (letterRow shl 1) + if (Point.of(x, y) in points) 1 else 0
                        }
                    }
                        .let { letterId -> LETTER_MAPPING.getOrElse(letterId) { " " } }
                }
            }

    private fun Sequence<String>.parse(): List<Vector> =
        this.map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
            .map { it.map(String::toInt) }
            .map { (px, py, vx, vy) -> Vector(px, py, vx, vy) }
            .toList()

    private data class Vector(val point: Point, val velocity: Point) {
        constructor(x: Int, y: Int, vx: Int, vy: Int) : this(Point.of(x, y), Point.of(vx, vy))
    }
}

package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point

class Day06 : Day(title = "Chronal Coordinates") {
    companion object Configuration {
        private const val MAX_DISTANCE_SUM = 10000
    }

    override fun first(input: Sequence<String>): Any = input
        .parse()
        .let { (points, min, max) ->
            min.createGridTo(max)
                .mapNotNull { gridPoint -> points.findNearest(gridPoint) }
                .groupBy { it.second }
                .values
                .asSequence()
                .filter { gridPoints -> gridPoints.none { (point, _) -> point.onEdge(min, max) } }
                .map { it.size }
                .max() ?: 0
        }


    override fun second(input: Sequence<String>): Any = input
        .parse()
        .let { (points, min, max) ->
            val xLimit = (MAX_DISTANCE_SUM - (max.x - min.x)) / points.size
            val yLimit = (MAX_DISTANCE_SUM - (max.y - min.y)) / points.size
            Point.of(min.x - xLimit, min.y - yLimit)
                .createGridTo(Point.of(max.x + xLimit, max.y + yLimit))
                .count { gp -> points.sumBy { p -> p.manhattan(gp) } < MAX_DISTANCE_SUM }
        }

    private fun Sequence<String>.parse(): Triple<List<Point>, Point, Point> {
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        return this@parse
            .map { it.split(", ").map(String::toInt) }
            .map { (x, y) ->
                if (x > maxX) maxX = x
                if (y > maxY) maxY = y
                if (x < minX) minX = x
                if (y < minY) minY = y
                Point.of(x, y)
            }
            .toList()
            .let { Triple(it, Point.of(minX, minY), Point.of(maxX, maxY)) }
    }

    private fun List<Point>.findNearest(origin: Point): Pair<Point, Point>? = this
        .fold(
            Closest(Pair(Point.ZERO_ZERO, Int.MAX_VALUE), false)
        ) { closest, destination ->
            destination.manhattan(origin).let { distance ->
                when {
                    distance < closest.coord.second -> Closest(destination to distance)
                    distance == closest.coord.second -> closest.apply { valid = false }
                    else -> closest
                }
            }
        }
        .takeIf { it.valid }
        ?.let { origin to it.coord.first }

    private fun Point.onEdge(min: Point, max: Point): Boolean =
        x == min.x || x == max.x || y == min.y || y == max.y

    private fun Point.createGridTo(p: Point, including: Boolean = true): Sequence<Point> =
        if (this == p) sequenceOf(this)
        else (p.x - x + if (including) 1 else 0).let { width ->
            var n = 0
            generateSequence { Point.of(x + n % width, y + n / width).also { n++ } }
                .take((p.y - y + if (including) 1 else 0) * width)
        }

    data class Closest(var coord: Pair<Point, Int>, var valid: Boolean = true)
}

package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.applyIf

class Day06 : Day(title = "Chronal Coordinates") {
    companion object Configuration {
        private const val MAX_DISTANCE_SUM = 10000
    }

    override fun first(input: Sequence<String>): Any = input
        .parse()
        .let { (points, min, max) ->
            min.createGridTo(max)
                .mapNotNull { gridPoint ->
                    points.fold(mutableListOf<Pair<Point, Int>>()) { nearest, c ->
                        c.manhattan(gridPoint).let { distance ->
                            if (nearest.isNotEmpty() && distance < nearest.first().second) {
                                mutableListOf(c to distance)
                            } else {
                                nearest.applyIf({ isEmpty() || first().second == distance }) {
                                    add(c to distance)
                                }
                            }
                        }
                    }.takeIf { it.size == 1 }?.let { gridPoint to it.first().first }
                }
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
                .createGridTo(Point.of(max.x + xLimit, max.y + yLimit)).toList()
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

    private fun Point.onEdge(min: Point, max: Point): Boolean =
        x == min.x || x == max.x || y == min.y || y == max.y

    private fun Point.createGridTo(p: Point, including: Boolean = true): Sequence<Point> =
        if (this == p) sequenceOf(this)
        else (p.x - x + if (including) 1 else 0).let { width ->
            var n = 0
            generateSequence { Point.of(x + n % width, y + n / width).also { n++ } }
                .take((p.y - y + if (including) 1 else 0) * width)
        }
}


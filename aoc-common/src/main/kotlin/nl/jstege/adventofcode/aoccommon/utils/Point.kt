package nl.jstege.adventofcode.aoccommon.utils

import kotlin.math.abs

/**
 * Represents a point with a x and an y value. This class is immutable. Any
 * addition or subtraction will result in a new [Point] object.
 * @author Jelle Stege
 *
 * @property x The x value for this point.
 * @property y The Y value for this property.
 *
 * @constructor Initializes a [Point] for the given x and y coordinates.
 */
data class Point internal constructor(val x: Int, val y: Int) : Comparable<Point> {
    /**
     * Defines some constants and some builder functions.
     */
    companion object {
        /**
         * Represents a origin of (0,0)
         */
        val ZERO_ZERO = Point.of(0, 0)

        /**
         * Represents a coordinate at (1,1)
         */
        val ONE_ONE = Point.of(1, 1)

        /**
         * Creates a [Point] of the given x and y values.
         *
         * @param x The x value to use.
         * @param y The y value to use.
         * @return A new point with corresponding x and y values.
         */
        fun of(x: Int, y: Int) = Point(x, y)

        /**
         * Creates a [List] of [Point]s for the given ranges.
         *
         * @param xs The x values to use.
         * @param ys The y values to use.
         * @return A [List] of [Point]s for the given ranges.
         */
        fun of(xs: IntProgression, ys: IntProgression) =
            xs.flatMap { x -> ys.map { y -> Point.of(x, y) } }

        /**
         * Creates a [Point] of the given [Pair], using the first value
         * as x and the second value as y.
         *
         * @param p The pair to convert to a [Point]
         * @return A new point with corresponding x and y values.
         */
        fun of(p: Pair<Int, Int>) = Point(p.first, p.second)

        private val NEIGHBORS8 = setOf(
            Point.of(-1, -1), Point.of(0, -1), Point.of(1, -1),
            Point.of(-1, 0), Point.of(1, 0),
            Point.of(-1, 1), Point.of(0, 1), Point.of(1, 1)
        )

        private val NEIGHBORS4 = setOf(
            Point.of(0, -1), Point.of(-1, 0), Point.of(1, 0), Point.of(0, 1)
        )
    }

    /**
     * All adjecent [Point]s, includes the 4 orthogonal and 4 diagonal directions.
     */
    val neighbors8: Set<Point> by lazy { NEIGHBORS8.map { this + it }.toSet() }

    /**
     * All adjecent [Point]s without diagonals, thus only the 4 orthogonal directions.
     */
    val neighbors4: Set<Point> by lazy { NEIGHBORS4.map { this + it }.toSet() }

    /**
     * Returns the point based off the [Direction] given
     */
    fun moveDirection(direction: Direction) = this + direction.mod

    /**
     * Returns the point based off the [Direction] given, which is the amount of steps away.
     */
    fun moveDirection(direction: Direction, steps: Int) = this + (direction.mod * steps)

    /**
     * Returns the manhattan distance between this [Point] and the other [Point]
     */
    fun manhattan(other: Point) = abs(x - other.x) + abs(y - other.y)

    /**
     * Adds some values to the x and y values of this point.
     * Results in a new point.
     * @param ax The value to add to the x value of this point.
     * @param ay the value to add to the y value of this point.
     * @return A new [Point] with the new values.
     */
    fun add(ax: Int, ay: Int) = Point.of(x + ax, y + ay)

    /**
     * Adds two points, results in a new object.
     * @param p the point to add.
     * @return A new [Point] with the new values.
     */
    fun add(p: Point) = add(p.x, p.y)

    /**
     * Adds a value to the x value of this point. Results in a new object.
     * @param ax the value to add to the x value of this point.
     * @return A new [Point] with the new values.
     */
    fun addX(ax: Int) = add(ax, 0)

    /**
     * Adds a value to the y value of this point. Results in a new object.
     * @param ay the value to add to the y value of this point.
     * @return A new [Point] with the new values.
     */
    fun addY(ay: Int) = add(0, ay)

    /**
     * Increments the x value of this point by 1. Results in a new object.
     * @return A new [Point] with the new values.
     */
    fun incX() = add(1, 0)

    /**
     * Increments the y value of this point by 1. Results in a new object.
     * @return A new [Point] with the new values.
     */
    fun incY() = add(0, 1)

    /**
     * Subtracts some values from the x and y values of this point.
     * Results in a new point.
     * @param sx The value to subtract from the x value of this point.
     * @param sy the value to subtract from to the y value of this point.
     * @return A new [Point] with the new values.
     */
    fun sub(sx: Int, sy: Int) = add(-sx, -sy)

    /**
     * Subtracts two points, results in a new object.
     * @param p the point to subtract.
     * @return A new [Point] with the new values.
     */
    fun sub(p: Point) = sub(p.x, p.y)

    /**
     * Subtracts a value from the x value of this point.
     * Results in a new object.
     * @param sx the value to subtract from the x value of this point.
     * @return A new [Point] with the new values.
     */
    fun subX(sx: Int) = sub(sx, 0)

    /**
     * Subtracts a value from the y value of this point.
     * Results in a new object.
     * @param sy the value to subtract from the y value of this point.
     * @return A new [Point] with the new values.
     */
    fun subY(sy: Int) = sub(0, sy)

    /**
     * Decrements the x value of this point by 1. Results in a new object.
     * @return A new [Point] with the new values.
     */
    fun decX() = sub(1, 0)

    /**
     * Decrements the y value of this point by 1. Results in a new object.
     * @return A new [Point] with the new values.
     */
    fun decY() = sub(0, 1)

    /**
     * Adds two points, results in a new object.
     * @param p the point to add.
     * @return A new [Point] with the new values.
     */
    operator fun plus(p: Point) = add(p)

    /**
     * Subtracts two points, results in a new object.
     * @param p the point to subtract.
     * @return A new [Point] with the new values.
     */
    operator fun minus(p: Point) = sub(p)

    /**
     * Performs a scalar multiplication on this [Point]
     * @param n The integer to multiply the point with.
     * @return a new [Point] with the new values.
     */
    operator fun times(n: Int) = Point.of(x * n, y * n)

    /**
     * Compares this point to another point.
     * @param other The other point.
     * @return 0 if the points are equal, < 0 if this point is smaller than the other and > 0 if
     * this point is bigger than the other.
     */
    override fun compareTo(other: Point): Int = when {
        this === other -> 0
        else -> x.compareTo(other.x).let { if (it != 0) it else y.compareTo(other.y) }
    }

    /**
     * Converts this point to a String representation.
     * @return The String representation for this point, which is of the type (x, y)
     */
    override fun toString(): String = "($x,$y)"
}

fun pointOf(x: Int, y: Int) = Point.of(x, y)
fun pointOf() = Point.ZERO_ZERO
fun pointOf(xs: IntProgression, ys: IntProgression) = Point.of(xs, ys)
fun pointOf(p: Pair<Int, Int>) = Point.of(p)

package nl.jstege.adventofcode.aoccommon.utils

/**
 * Represents a point with a x and an y value. This class is immutable. Any
 * addition or subtraction will result in a new [Point] object.
 * @author Jelle Stege
 */
data class Point(val x: Int, val y: Int) : Comparable<Point> {
    companion object Constants {
        val ZERO_ZERO = Point(0, 0)
        val ONE_ONE = Point(1, 1)

        fun of(x: Int, y: Int) = Point(x, y)
        fun of(p: Pair<Int, Int>) = Point(p.first, p.second)
    }

    /**
     * Adds some values to the x and y values of this point.
     * Results in a new point.
     * @param ax The value to add to the x value of this point.
     * @param ay the value to add to the y value of this point.
     */
    fun add(ax: Int, ay: Int) = Point(x + ax, y + ay)

    /**
     * Adds two points, results in a new object.
     * @param p the point to add.
     */
    fun add(p: Point) = add(p.x, p.y)

    /**
     * Adds a value to the x value of this point. Results in a new object.
     * @param ax the value to add to the x value of this point.
     */
    fun addX(ax: Int) = add(ax, 0)

    /**
     * Adds a value to the y value of this point. Results in a new object.
     * @param ay the value to add to the y value of this point.
     */
    fun addY(ay: Int) = add(0, ay)

    /**
     * Subtracts some values from the x and y values of this point.
     * Results in a new point.
     * @param sx The value to subtract from the x value of this point.
     * @param sy the value to subtract from to the y value of this point.
     */
    fun sub(sx: Int, sy: Int) = add(sx * -1, sy * -1)

    /**
     * Subtracts two points, results in a new object.
     * @param p the point to subtract.
     */
    fun sub(p: Point) = sub(p.x, p.y)

    /**
     * Subtracts a value from the x value of this point.
     * Results in a new object.
     * @param sx the value to subtract from the x value of this point.
     */
    fun subX(sx: Int) = sub(sx, 0)

    /**
     * Subtracts a value from the y value of this point.
     * Results in a new object.
     * @param sy the value to subtract from the y value of this point.
     */
    fun subY(sy: Int) = sub(0, sy)

    /**
     * Adds two points, results in a new object.
     * @param p the point to add.
     */
    operator fun plus(p: Point) = add(p)

    /**
     * Subtracts two points, results in a new object.
     * @param p the point to subtract.
     */
    operator fun minus(p: Point) = sub(p)

    /**
     * Compares this point to another point.
     * @param other The other point.
     * @return 0 if the points are equal, < 0 if this point is smaller than the other and > 0 if
     * this point is bigger than the other.
     */
    override fun compareTo(other: Point): Int {
        if (this === other) {
            return 0
        }

        val cX = x.compareTo(other.x)
        return if (cX != 0) {
            cX
        } else {
            y.compareTo(other.y)
        }
    }

    /**
     * Converts this point to a String representation.
     *
     */
    override fun toString(): String {
        return "($x,$y)"
    }
}
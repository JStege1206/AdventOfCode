package nl.jstege.adventofcode.aoccommon.utils

/**
 * Specifies a certain cardinal moveDirection
 * @author Jelle Stege
 */
enum class Direction constructor(val mod: Point) {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    constructor(x: Int, y: Int) : this(Point.of(x, y))

    val opposite
        get() = when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
}

package nl.jstege.adventofcode.aoccommon.utils

/**
 * Specifies a certain cardinal moveDirection
 * @author Jelle Stege
 */
enum class Direction constructor(x: Int, y: Int) {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    val mod = Point.of(x, y)

    val opposite by lazy {
        when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }

    val forward by lazy { this }

    val right by lazy {
        when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }

    val back by lazy { this.opposite }

    val left by lazy {
        when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
    }
}

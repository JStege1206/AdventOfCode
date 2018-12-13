package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.Direction
import nl.jstege.adventofcode.aoccommon.utils.Point
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

class Day13 : Day(title = "Mine Cart Madness") {
    override fun first(input: Sequence<String>): Any =
        input.toList().parse().let { carts ->
            carts.toList().findFirstCrash()
                ?.let { (x, y) -> "$x,$y" } ?: throw IllegalStateException()
        }


    override fun second(input: Sequence<String>): Any =
        input.toList().parse().let { carts ->
            carts.toList().findLastCart()
                ?.let { (x, y) -> "$x,$y" } ?: throw IllegalStateException()
        }

    private tailrec fun List<Cart>.findFirstCrash(): Point? {
        if (size <= 1) return null

        val locations = this.map { it.location }.toMutableSet()

        for (cart in this.sortedBy { it.index }) {
            locations -= cart.location
            cart.next()

            if (cart.location !in locations) {
                locations += cart.location
            } else {
                return cart.location
            }
        }

        return this.filter { it.location in locations }.findFirstCrash()
    }

    private tailrec fun List<Cart>.findLastCart(): Point? {
        if (size == 0) return null
        if (size == 1) return head.location

        val newLocations = this
            .sortedBy { it.index }
            .transformTo(this.map { it.location }.toMutableSet()) { locations, cart ->
                if (cart.location in locations) {
                    locations -= cart.location
                    cart.next()

                    if (cart.location in locations) {
                        locations -= cart.location
                    } else {
                        locations += cart.location
                    }
                }
            }

        return this.filter { it.location in newLocations }.findLastCart()
    }

    private fun List<String>.parse(): List<Cart> =
        Grid(
            this.head.length,
            this.flatMap { it.toList() }.toCharArray()
        ).extractCarts()


    class Grid(val width: Int, private val grid: CharArray) {
        operator fun get(p: Point) = get(p.x, p.y)
        operator fun get(x: Int, y: Int) = grid[toIndex(x, y)]
        operator fun set(p: Point, v: Char) = set(p.x, p.y, v)
        operator fun set(x: Int, y: Int, v: Char) {
            grid[toIndex(x, y)] = v
        }

        private fun toIndex(x: Int, y: Int): Int = y * width + x
        private fun fromIndex(i: Int) = Point.of(i % width, i / width)

        fun extractCarts(): List<Cart> = grid
            .withIndex()
            .transformTo(mutableListOf()) { carts, (i, c) ->
                if (c in "><^v") {
                    val (dir, newC) = when (c) {
                        '^' -> Direction.NORTH to '|'
                        '>' -> Direction.EAST to '-'
                        'v' -> Direction.SOUTH to '|'
                        '<' -> Direction.WEST to '-'
                        else -> throw IllegalStateException()
                    }
                    carts += Cart(fromIndex(i), dir, this)
                    grid[i] = newC
                }
            }
    }


    @Suppress("MemberVisibilityCanBePrivate") //Can't make grid private as index is inlined.
    class Cart(var location: Point, private var direction: Direction, val grid: Grid) {
        private var intersectionModifier = 0
            get() = (field++ % 3)

        private inline val intersectionDirection: Direction
            get() = when (intersectionModifier) {
                0 -> direction.left
                1 -> direction
                2 -> direction.right
                else -> throw IllegalStateException()
            }

        inline val index get() = location.y * grid.width * location.x

        fun next() {
            //Can't use Point.moveDirection, north and south are reversed.
            location = when (direction) {
                Direction.NORTH -> location.decY()
                Direction.EAST -> location.incX()
                Direction.SOUTH -> location.incY()
                Direction.WEST -> location.decX()
            }

            direction = when (grid[location]) {
                '|', '-' -> direction
                '\\' -> when (direction) {
                    Direction.NORTH, Direction.SOUTH -> direction.left
                    Direction.EAST, Direction.WEST -> direction.right
                }
                '/' -> when (direction) {
                    Direction.NORTH, Direction.SOUTH -> direction.right
                    Direction.EAST, Direction.WEST -> direction.left
                }
                '+' -> intersectionDirection
                else -> throw IllegalStateException()
            }
        }
    }
}

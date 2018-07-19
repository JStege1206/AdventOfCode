package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import kotlin.math.abs
import kotlin.math.sqrt

/**
 *
 * @author Jelle Stege
 */
class Day20 : Day(title = "Particle Swarm") {
    private companion object Configuration {
        private const val TIME_LIMIT = 100000L
        private const val INPUT_PATTERN_STRING = """[a-z]=<(-?\d+),(-?\d+),(-?\d+)>"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
        private val ORIGIN = Point3D(0, 0, 0)
    }

    override fun first(input: Sequence<String>): Any {
        return input.parse()
            .withIndex()
            .minBy { (_, it) -> it.positionAtTime(TIME_LIMIT).manhattan(ORIGIN) }!!
            .index
    }

    override fun second(input: Sequence<String>): Any {
        val particles = input.parse().toList()
        particles.forEachIndexed { i, p1 ->
            particles.drop(i + 1).forEach { p2 ->
                val t = p1.collidesWith(p2)
                if (t != null) {
                    p1.removed = true
                    p2.removed = true
                }
            }
        }

        return particles.count { !it.removed }
    }

    private fun Sequence<String>.parse() = this
        .map { it.split(", ") }
        .map { (rp, rv, ra) ->
            val p = INPUT_REGEX.matchEntire(rp)!!.groupValues
                .drop(1) //Drop the first match, the whole string
                .map { it.toLong() } //All remaining elements are numbers
                .let { (x, y, z) -> Point3D(x, y, z) }
            val v = INPUT_REGEX.matchEntire(rv)!!.groupValues
                .drop(1) //Drop the first match, the whole string
                .map { it.toLong() } //All remaining elements are numbers
                .let { (x, y, z) -> Point3D(x, y, z) }
            val a = INPUT_REGEX.matchEntire(ra)!!.groupValues
                .drop(1) //Drop the first match, the whole string
                .map { it.toLong() } //All remaining elements are numbers
                .let { (x, y, z) -> Point3D(x, y, z) }
            Particle(p, v, a)
        }

    private data class Point3D(val x: Long, val y: Long, val z: Long) {
        fun manhattan(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

        operator fun times(n: Long) = Point3D(x * n, y * n, z * n)
        operator fun plus(o: Point3D) = Point3D(x + o.x, y + o.y, z + o.z)
    }

    private data class Particle(
        val p: Point3D,
        val v: Point3D,
        val a: Point3D,
        var removed: Boolean = false
    ) {
        fun positionAtTime(t: Long): Point3D = a * (t * (t + 1) / 2) + v * t + p

        /**
         * Particle's position is represented by the formula used in [Particle.positionAtTime],
         * which is `pos_t=a * t * (t + 1) / 2 + v * t + p`. This is a second degree quadratic
         * functions, which can thus be solved by the quadratic equation. To solve this first we
         * rewrite the formula used to the following: `pos_t= a/2 * t * t + (a/2 + v) * t + p`.
         *
         * The quadratic equation is further restricted by the following requirements:
         * - All t >= 0
         * - All t >= are integers.
         * - For all t >= 0 it must be so that p1.positionAtTime(t) == p2.positionAtTime(t)
         *
         * Since p1.positionAtTime(t) == p2.positionAtTime(t), it means that p1.x(t) == p2.x(t),
         * therefore, we can start by checking if there is a collision in x values and simply use
         * the formula given above to check if the other dimensions collide as well.
         *
         * So for p1.x = p2.x we have:
         * p1a.x/2*t*t + p1a.x/2*p1v.x*t + p1p.x = p2a.x/2*t*t + p2a.x*p2v.x*t + p2p.x
         * from which we can deduce:
         * - A = p1a.x/2 - p2a.x / 2 == (p1a.x - p2a.x) / 2
         * - B = p1a.x/2*p1v.x - p2a.x/2*p2v.x
         * - C = p1p.x - p2p.x
         * Since this can result in non-integer divisions we define A' and B' such that:
         * - A' = 2 * A = p1a.x - p2a.x
         * - B' = 2 * B = (p1a.x + p1v.x*2) - (p2a.x + p2v.x*2)
         * The quadratic equation then becomes:
         * t=(-B +/- sqrt(B^2 -4*A*C))/(2*A)
         * Since we're using A' and B' it becomes:
         * t=(-(2*B) +/- sqrt((2*B) - 4*(2*A)*C))/(2*(2*A))=(-2*B +/- sqrt((2*B) - 16*A*C))/(4*A))
         *  =(-B' +/- sqrt(B' - 8 * A' * C)) / (2 * A')
         *
         *  @param other The particle which may collide with this particle.
         *  @return The first value for which the given particles collide, always >= 0 when present,
         *  if no collision occurs, returns null.
         */
        fun collidesWith(other: Particle): Long? {
            val candidates = mutableSetOf<Long>()
            // Calculate A'
            val ap = a.x - other.a.x
            // Calculate B'
            val bp = (2 * v.x + a.x) - (2 * other.v.x + other.a.x)
            val c = p.x - other.p.x

            if (ap == 0L && bp != 0L) {
                // Calculate collisions for B * t + C = 0, only add candidate if it is an integer.
                if ((c * -2) % bp == 0L) {
                    candidates.add(-2 * c / bp)
                }
            } else if (ap == 0L && bp == 0L) {
                // Calculate collisions for C = 0, when C != 0 return null as it is not possible to
                // have a collision.
                return if (c == 0L) 0 else null
            } else {
                // Use regular quadratic equation as provided in the KDoc, using A' and B'. Take
                // note that only integer values may be considered.
                val dp = bp * bp - 8 * ap * c
                // If discriminant < 0 there are no collisions
                if (dp < 0) {
                    return null
                }
                val s = sqrt(dp.toDouble()).toLong()

                // Check if the discriminant is a perfect square, if not, no collisions occur in
                // integer space.
                if (s * s != dp) {
                    return null
                }

                val n1 = -bp + s
                val n2 = -bp - s

                // Final division of the quadratic equation. Check if integer and >= 0, if so,
                // consider as a candidate.
                if (n1 % (2 * ap) == 0L) {
                    (n1 / (2 * ap)).let { if (it >= 0) candidates.add(it) }
                }
                if (s != 0L && n2 % (2 * ap) == 0L) {
                    (n2 / (2 * ap)).let { if (it >= 0) candidates.add(it) }
                }
            }

            // We have only checked the x values of the particle, calculating the 3D position of
            // all candidates give us our real collisions. We need the first, so find that one.
            return candidates
                .filter { this.positionAtTime(it) == other.positionAtTime(it) }
                .min()
        }
    }
}

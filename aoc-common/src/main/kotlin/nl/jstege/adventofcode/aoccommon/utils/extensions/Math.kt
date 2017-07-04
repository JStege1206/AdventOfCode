package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */

/**
 * Returns the floor modulus of the arguments.
 * @see Math.floorMod
 *
 * @receiver The divisor for the floor modulus operation
 * @param y The dividend for the floor modulus operation
 * @return The floor modulus of the given arguments.
 */
infix fun Long.floorMod(y: Long): Long = Math.floorMod(this, y)

/**
 * Allows an infix notation of a power method.
 * @see Math.pow
 *
 * @receiver The base of the power operation.
 * @param n The exponent of the power operation.
 * @return The result of receiver ^ n, as an integer.
 */
infix fun Int.pow(n: Int): Int = Math.pow(this.toDouble(), n.toDouble()).toInt()

/**
 * Fast access to a logarithm function with a definable base.
 * @see Math.log
 *
 * @param d The value to take the logarithm of.
 * @param base The base of the logarithm, defaults to e.
 */
fun log(d: Double, base: Double = Math.E): Double = Math.log(d) / Math.log(base)


/**
 * Fast access to a logarithm function with a definable base. For integers.
 * @see Math.log
 *
 * @param n The value to take the logarithm of.
 * @param base The base of the logarithm, defaults to 10.
 */
fun log(n: Int, base: Int = 10): Int = log(n.toDouble(), base.toDouble()).toInt()
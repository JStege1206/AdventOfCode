package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Extra utilities for numbers.
 * @author Jelle Stege
 */

/**
 * @return The byte as an unsigned integer.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Byte.toUnsignedInt(): Int = this.toInt() and 0xFF

/**
 * Counts all set bits in the integer.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.bitCount(): Int = Integer.bitCount(this)

/**
 * Counts all set bits in the long.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Long.bitCount(): Int = java.lang.Long.bitCount(this)

/**
 * Calculates the factorial of the given Long
 */
tailrec fun Long.factorial(acc: Long = 1): Long = when (this) {
    0L -> acc
    else -> (this - 1).factorial(acc * this)
}

/**
 * Calculates the factorial of the given Integer
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.factorial(): Long = this.toLong().factorial()

/**
 * Returns the floor modulus of the arguments.
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun Long.floorMod(y: Long): Long = Math.floorMod(this, y)

/**
 * @return The highest power of 2 possible in this integer.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.highestOneBit(): Int = Integer.highestOneBit(this)

/**
 * @return True if this integer is even
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.isEven(): Boolean = this % 2 == 0

/**
 * @return True if this integer is odd
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * @return the character representation of this integer.
 */
fun Int.toHexChar(): Char = nl.jstege.adventofcode.aoccommon.utils.extensions.HEX_CHARS[this]
private val HEX_CHARS = "0123456789abcdef".toCharArray()

/**
 * Converts a byte array to a hexadecimal string representation.
 */
fun ByteArray.toHexString(n: Int = this.size): String {
    val ca = CharArray(Math.min(this.size, n) * 2)
    var i = 0
    while (i < Math.min(this.size, n)) {
        ca[i * 2] = (this[i].toUnsignedInt() ushr 4).toHexChar()
        ca[i * 2 + 1] = (this[i].toUnsignedInt() and 0x0F).toHexChar()
        i++
    }
    return String(ca)
}

/**
 * Allows an infix notation of a power method.
 */
@Suppress("NOTHING_TO_INLINE") inline
infix fun Int.pow(n: Int): Int = Math.pow(this.toDouble(), n.toDouble()).toInt()

/**
 * Fast access to a logarithm function with a definable base.
 */
@Suppress("NOTHING_TO_INLINE") inline
fun log(d: Double, base: Double): Double = Math.log(d) / Math.log(base)

/**
 * Fast access to a logarithm function with a definable base. For integers.
 */
@Suppress("NOTHING_TO_INLINE") inline
fun log(n: Int, base: Int): Int = nl.jstege.adventofcode.aoccommon.utils.extensions.log(n.toDouble(), base.toDouble()).toInt()

/**
 * Allows the infix xor operation on BigIntegers
 */
@Suppress("NOTHING_TO_INLINE") inline
infix fun java.math.BigInteger.xor(n: java.math.BigInteger): java.math.BigInteger = this.xor(n)

/**
 * Allows the infix and operation on BigIntegers
 */
@Suppress("NOTHING_TO_INLINE") inline
infix fun java.math.BigInteger.and(n: java.math.BigInteger): java.math.BigInteger = this.and(n)

/**
 * Allows the infix or operation on BigIntegers
 */
@Suppress("NOTHING_TO_INLINE") inline
infix fun java.math.BigInteger.or(n: java.math.BigInteger): java.math.BigInteger = this.or(n)

/**
 * Allows the infix shl operation on BigIntegers
 */
@Suppress("NOTHING_TO_INLINE") inline
infix fun java.math.BigInteger.shl(n: Int): java.math.BigInteger = this.shiftLeft(n)

/**
 * Allows the infix shr operation on BigIntegers
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun java.math.BigInteger.shr(n: Int): java.math.BigInteger = this.shiftRight(n)

fun <E: Comparable<E>> min(vararg number: E): E {
    return number.minBy { it }!!
}
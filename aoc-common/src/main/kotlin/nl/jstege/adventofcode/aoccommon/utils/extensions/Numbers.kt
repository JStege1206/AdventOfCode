package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.math.BigInteger

/**
 * Extra utilities for numbers.
 * @author Jelle Stege
 */

/**
 * @return The byte as an unsigned integer.
 */
fun Byte.toUnsignedInt(): Int = this.toInt() and 0xFF

/**
 * Counts all set bits in the integer.
 */
fun Int.bitCount(): Int = Integer.bitCount(this)

/**
 * Counts all set bits in the long.
 */
fun Long.bitCount(): Int = java.lang.Long.bitCount(this)

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
fun Int.factorial(): Long = this.toLong().factorial()

/**
 * Returns the floor modulus of the arguments.
 */
infix fun Long.floorMod(y: Long): Long = Math.floorMod(this, y)

/**
 * @return The highest power of 2 possible in this integer.
 */
fun Int.highestOneBit(): Int = Integer.highestOneBit(this)

/**
 * @return True if this integer is even
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * @return True if this integer is odd
 */
fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * @return the character representation of this integer.
 */
fun Int.toHexChar(): Char = HEX_CHARS[this]

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
infix fun Int.pow(n: Int): Int = Math.pow(this.toDouble(), n.toDouble()).toInt()

/**
 * Fast access to a logarithm function with a definable base.
 */
fun log(d: Double, base: Double): Double = Math.log(d) / Math.log(base)

/**
 * Fast access to a logarithm function with a definable base. For integers.
 */
fun log(n: Int, base: Int): Int = log(n.toDouble(), base.toDouble()).toInt()

/**
 * Allows the infix xor operation on BigIntegers
 */
infix fun BigInteger.xor(n: BigInteger): BigInteger = this.xor(n)

/**
 * Allows the infix and operation on BigIntegers
 */
infix fun BigInteger.and(n: BigInteger): BigInteger = this.and(n)

/**
 * Allows the infix or operation on BigIntegers
 */
infix fun BigInteger.or(n: BigInteger): BigInteger = this.or(n)

/**
 * Allows the infix shl operation on BigIntegers
 */
infix fun BigInteger.shl(n: Int): BigInteger = this.shiftLeft(n)

/**
 * Allows the infix shr operation on BigIntegers
 */
infix fun BigInteger.shr(n: Int): BigInteger = this.shiftRight(n)

fun <E : Comparable<E>> min(vararg elements: E): E = elements.minBy { it }!!

fun <E : Comparable<E>> max(vararg elements: E): E = elements.maxBy { it }!!

fun mid(a: Int, b: Int, c: Int) = Math.max(Math.min(a, b), Math.min(Math.max(a, b), c))
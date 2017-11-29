package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Extra utilities for numbers.
 * @author Jelle Stege
 */

/**
 * Converts the receiving byte to an unsigned integer.
 *
 * @receiver The byte to convert.
 * @return The byte as an unsigned integer.
 */
fun Byte.toUnsignedInt(): Int = this.toInt() and 0xFF

/**
 * Calculates the factorial of the given Long.
 *
 * @receiver The argument for the factorial function.
 * @returns The factorial result.
 */
fun Long.factorial(): Long = this.factorial(1)

private tailrec fun Long.factorial(acc: Long = 1): Long = when (this) {
    0L -> acc
    else -> (this - 1).factorial(acc * this)
}

/**
 * Determines if the receiving integer is even.
 *
 * @receiver The integer to check.
 * @return True if this integer is even
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Determines if the receiving integer is odd.
 *
 * @receiver The integer to check.
 * @return True if this integer is odd
 */
fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * Casts an integer x within range 0 <= x <= 15 to it's hexadecimal character.
 *
 * @receiver The integer to cast.
 * @return the character representation of this integer.
 *
 * @throws ArrayIndexOutOfBoundsException if the given integer x is not within range 0 <= x <= 15
 */
fun Int.toHexChar(): Char = hexChars[this]
private val hexChars = "0123456789abcdef".toCharArray()

/**
 * Counts all set bits in the integer.
 * @receiver The integer to count 1 bits in.
 * @returns The amount of bits set to 1.
 */
fun Int.bitCount(): Int = Integer.bitCount(this)

/**
 * Counts all set bits in the long.
 * @receiver The long to count 1 bits in.
 * @returns The amount of bits set to 1.
 */
fun Long.bitCount(): Int = java.lang.Long.bitCount(this)


/**
 * Returns the int value with at most a single one-bit in the highest position of the given int.
 *
 * @receiver The integer to determine the highest 1-bit of.
 * @return The highest power of 2 possible in this integer.
 */
fun Int.highestOneBit(): Int = Integer.highestOneBit(this)

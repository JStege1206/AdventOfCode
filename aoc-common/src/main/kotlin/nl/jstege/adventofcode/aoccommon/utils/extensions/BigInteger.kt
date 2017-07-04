package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.math.BigInteger

/**
 * Extension methods for BigIntegers.
 * @author Jelle Stege
 */

/**
 * Allows the infix xor operation on BigIntegers
 * @see java.math.BigInteger.xor
 *
 * @receiver The first BigInteger
 * @param n The other BigInteger
 * @return receiver ^ n
 */
infix fun BigInteger.xor(n: BigInteger): BigInteger = this.xor(n)

/**
 * Allows the infix and operation on BigIntegers
 * @see java.math.BigInteger.and
 *
 * @receiver The first BigInteger
 * @param n The other BigInteger
 * @return receiver & n
 */
infix fun BigInteger.and(n: BigInteger): BigInteger = this.and(n)

/**
 * Allows the infix or operation on BigIntegers
 * @see java.math.BigInteger.or
 *
 * @receiver The first BigInteger
 * @param n The other BigInteger
 * @return receiver | n
 */
infix fun BigInteger.or(n: BigInteger): BigInteger = this.or(n)

/**
 * Allows the infix shl operation on BigIntegers
 * @see java.math.BigInteger.shiftLeft
 *
 * @receiver The BigInteger
 * @param n The amount of bits to shift left
 * @return receiver << n
 */
infix fun BigInteger.shl(n: Int): BigInteger = this.shiftLeft(n)

/**
 * Allows the infix shr operation on BigIntegers
 * @see java.math.BigInteger.shiftRight
 *
 * @receiver The BigInteger
 * @param n The amount of bits to shift right
 * @return receiver << n
 */
infix fun BigInteger.shr(n: Int): BigInteger = this.shiftRight(n)


/**
 * Converts a String to a BigInteger.
 * @see java.math.BigInteger
 *
 * @receiver The string to convert
 * @param radix The radix to use, defaults to 10
 * @return A BigInteger instance, or a NumberFormatException if the given String can not be parsed.
 */
fun String.toBigInteger(radix: Int = 10): BigInteger = BigInteger(this, radix)
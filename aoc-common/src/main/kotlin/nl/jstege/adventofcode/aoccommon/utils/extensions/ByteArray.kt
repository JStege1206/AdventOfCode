package nl.jstege.adventofcode.aoccommon.utils.extensions

import kotlin.experimental.and

/**
 * Extension methods for ByteArrays.
 * @author Jelle Stege
 */

/**
 * Checks whether the [ByteArray] starts with a certain amount of zeroes. This includes checking
 * partial bytes (words) in case the given amount is an uneven number.
 *
 * @receiver The [ByteArray] to check.
 * @param amount The amount of zeroes the given ByteArray should start with.
 * @return True if the given array starts with the given amount of zeroes, false if otherwise.
 */
fun ByteArray.prefixedWithZeroes(amount: Int) = when {
    amount > this.size * 2 -> false
    else -> this.asSequence()
            .take(amount / 2 + if (amount.isOdd()) 1 else 0)
            .flatMap { sequenceOf(it.toInt() ushr 4, it.toInt() and 0x0F) }
            .withIndex()
            .all { (i, it) -> i == amount || it == 0 }
}

/**
 * Converts (part of) a byte array to a hexadecimal string representation.
 *
 * @receiver The [ByteArray] to convert.
 * @param n The amount of bytes to convert, defaults to the size of the array.
 * @return The string representation of the given ByteArray in hexadecimal format.
 */
fun ByteArray.toHexString(n: Int = this.size): String =
        CharArray(Math.min(this.size, n) * 2) {
            if (it.isEven()) (this[it / 2].toUnsignedInt() ushr 4).toHexChar()
            else (this[it / 2].toUnsignedInt() and 0x0F).toHexChar()
        }.let { String(it) }

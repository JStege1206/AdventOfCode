package nl.jstege.adventofcode.aoccommon.utils.extensions

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
fun ByteArray.prefixedWithZeroes(amount: Int): Boolean {
    val checks = amount / 2
    val zero = 0.toByte()
    if (this.size < checks) return false
    (0 until checks)
            .filter { this[it] != zero }
            .ifPresent { return false }
    return amount.isEven() || (this.size >= checks + 1 && this[checks].toInt() and 0xF0 == 0)
}

/**
 * Converts (part of) a byte array to a hexadecimal string representation.
 *
 * @receiver The [ByteArray] to convert.
 * @param n The amount of bytes to convert, defaults to the size of the array.
 * @return The string representation of the given ByteArray in hexadecimal format.
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
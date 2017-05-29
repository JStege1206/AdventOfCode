package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.math.BigInteger

/**
 *
 * @author Jelle Stege
 */

fun String.replace(old: List<Char>, new: List<Char>): String =
        old.zip(new).fold(this, {s, p -> s.replace(p.first, p.second)})

fun String.toIntWithNegative(): Int =
        if (this[0] == '-') this.substring(1).toInt() * -1
        else this.toInt()

fun String.isCastableToInt(): Boolean = this[0].isDigit() || this[0] == '-' || this[0] == '+'

fun String.toBigInteger(radix: Int): BigInteger {
    return BigInteger(this, radix)
}
package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */

/**
 * Replaces all characters in the given old list with their respectable characters in the new list.
 * @receiver The String to perform the replacements on.
 * @param old The list of old characters.
 * @param new The list of new characters.
 * @return The String with all replacements performed on it.
 */
fun String.replace(old: List<Char>, new: List<Char>): String = old.zip(new)
        .fold(this, { s, (first, second) -> s.replace(first, second) })

fun String.replace(vararg replacements: Pair<Char, Char>): String = replacements
        .fold(this) { s, (old, new) -> s.replace(old, new) }

/**
 * Checks whether this string is castable to an integer. Also returns true if the first character
 * is either a - or + symbol.
 *
 * @receiver The String to check.
 * @return True if this String only exists of characters, optionally prepended by a - or + symbol.
 */
fun String.isCastableToInt(): Boolean = this.all { it.isDigit() }
        || this[0] == '-' || this[0] == '+'

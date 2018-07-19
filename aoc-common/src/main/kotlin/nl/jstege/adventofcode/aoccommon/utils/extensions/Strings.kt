package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.ObjectMapper

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
fun String.isCastableToInt(): Boolean =
    this.all { it.isDigit() } || this[0] == '-' || this[0] == '+'

/**
 * Convert the receiver to a JSON Object tree.
 *
 * @receiver The string to convert
 * @return A JSON object.
 */
fun String.toJson() = ObjectMapper().readTree(this)!!

/**
 * Extracts a value from a string by using the given regular expression
 *
 * @receiver The string to extract a value from.
 * @param regex The regular expression to use.
 * @param index The index of the value to be extracted, note that this index starts with 1, as 0
 * returns the entire matched string.
 * @return The extracted value.
 */
fun String.extractValue(regex: Regex, index: Int) =
    regex.matchEntire(this)?.groupValues?.get(index)
            ?: throw IllegalArgumentException("String does not match regex.")
/**
 * Extracts values from a string by using the given regular expression
 *
 * @receiver The string to extract a value from.
 * @param regex The regular expression to use.
 * @param indices The indices of the values to be extracted, note that these indices start at 1, as 
 * 0 returns the entire matched string.
 * @return The extracted values.
 */
fun String.extractValues(regex: Regex, vararg indices: Int) =
    regex.matchEntire(this)?.groupValues?.slice(indices.asIterable())
            ?: throw IllegalArgumentException("String does not match regex.")

package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

/**
 *
 * @author Jelle Stege
 */

/**
 * Replaces all characters in the given old list with their respectable characters in the new list.
 * @receiver The String to perform the replacements on.
 * @param replacements The replacements to perform, fashioned in an array consisting of (old, new)
 * pairs
 * @return The String with all replacements performed on it.
 */
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

/**
 * Repeats the given string [n] times.
 * @receiver String The string to repeat
 * @param n Int The amount of times to repeat the string. May not be negative.
 * @return String The given string, repeated [n] times.
 */
operator fun String.times(n: Int) = this.repeat(n)

/**
 * Checks if any of the given patterns exists in the receiver.
 * @receiver String The haystack to search in
 * @param set Collection<String> The needles to search for
 * @return Boolean True if any of the patterns exist in the given String.
 */
fun String.containsAny(set: Collection<String>) = set.any { it in this }

/**
 * Wraps a string to the given [wrapLength] line width.
 * @receiver String The string to wrap.
 * @param wrapLength Int The maximum length a line may be, unless [wrapLongWords] is false, in that
 * case a line might still be longer (if a word is longer than this parameter).
 * @param lineSeparator The line seperator to use.
 * @param wrapLongWords Boolean If true, will split up words longer than [wrapLength] over multiple
 * lines.
 * @return String The string, formatted so lines are no longer than [wrapLength] (unless
 * [wrapLongWords] is false and a word is longer than [wrapLength]).
 */
fun String.wrap(
    wrapLength: Int = 80,
    lineSeparator: String = "\n",
    wrapLongWords: Boolean = true
): String {
    if (this.length < wrapLength) {
        return this
    }

    //The result, to be built.
    val wrappedResult = StringBuilder()
    //All words, spaces and line separators in the given string.
    val tokens = StringTokenizer(this, " $lineSeparator", true)
    //The line currently being built.
    val wrappedLine = StringBuilder()
    tokenLoop@ while (tokens.hasMoreTokens()) {
        when (val token = tokens.nextToken()) {
            //On a line seperator wrap up the current line and add a line separator. Create a new
            //"current" line after that.
            lineSeparator -> {
                wrappedResult.appendln(wrappedLine, lineSeparator)
                wrappedLine.clear()
            }
            //On a space add it to the current line when possible. If not possible, create a new 
            //line and dont add the space.
            " " -> {
                if (wrappedLine.length < wrapLength) {
                    wrappedLine.append(token)
                } else {
                    wrappedResult.appendln(wrappedLine, lineSeparator)
                    wrappedLine.clear()
                }
            }
            //token is a regular word.
            else -> {
                //If the word fits within the current line add it. Note that spaces are handled
                //seperately. After that, just continue with the next token.
                if (wrappedLine.length + token.length <= wrapLength) {
                    wrappedLine.append(token)
                    continue@tokenLoop
                }

                //The word does not fit on the current line, determine what to do with it.
                if (!wrapLongWords || token.length <= wrapLength) {
                    //The word should not be split up, just finish up the current line and 
                    //create a new line with the long word.
                    if (wrappedLine.isNotEmpty()) {
                        wrappedResult.appendln(wrappedLine, lineSeparator)
                    }
                    wrappedLine.clear()
                    wrappedLine.append(token)
                } else {
                    //The word should be split up in shorter pieces.
                    val spaceLeft = max(wrapLength - wrappedLine.length, 0)
                    if (spaceLeft > 0) {
                        wrappedLine.append(token.substring(0, spaceLeft))
                    }
                    //Cut off the first part, possibly present on the previous line. Then chunk it
                    //up in segments which are at most wrapLength of size. Then create new lines for
                    //the result. If there is a small part left, it is kept in wrappedLine, after
                    //which other words can be added.
                    token.substring(spaceLeft)
                        .chunked(wrapLength)
                        .forEach {
                            wrappedResult.appendln(wrappedLine, lineSeparator)
                            wrappedLine.clear()
                            wrappedLine.append(it)
                        }
                }
            }
        }
    }
    //Append the last part line and return as string, as we're done.
    return wrappedResult.append(wrappedLine).toString()
}

/**
 * Appends a [StringBuilder] to this [StringBuilder] and a custom line seperator.
 * @receiver StringBuilder The [StringBuilder] to append to.
 * @param stringBuilder StringBuilder The [StringBuilder] to append.
 * @param lineSeparator String The line seperator to append after [stringBuilder] is appended.
 * @return StringBuilder the customized [StringBuilder], same as the receiver.
 */
private fun StringBuilder.appendln(
    stringBuilder: StringBuilder,
    lineSeparator: String = System.lineSeparator()
) = this.append(stringBuilder).append(lineSeparator)


fun String.splitOnIndex(index: Int): List<String> =
    listOf(this.substring(0, index), this.substring(index))

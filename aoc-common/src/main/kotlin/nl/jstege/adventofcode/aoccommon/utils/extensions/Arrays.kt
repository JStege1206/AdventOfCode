package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */
fun <E> CharArray.transformToIndexed(d: E, t: (Int, E, Char) -> Unit): E {
    var i = 0
    for (c in this) {
        t(i++, d, c)
    }
    return d
}

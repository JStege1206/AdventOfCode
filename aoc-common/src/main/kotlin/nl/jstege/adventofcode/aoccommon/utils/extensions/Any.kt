package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */
inline fun <E> E.applyIf(condition: Boolean, block: E.() -> Unit): E {
    if (condition) {
        block()
    }
    return this
}

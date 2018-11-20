package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */
inline fun <E> E.applyIf(condition: E.() -> Boolean, block: E.() -> Unit): E {
    if (this.condition()) {
        block()
    }
    return this
}

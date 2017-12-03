package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.coroutines.experimental.buildSequence

/**
 *
 * @author Jelle Stege
 */

/**
 * Applies an operation to a Sequence and emits each successive value.
 *
 * @param initial The initial value to be emitted, used as accumulative value for the operation.
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
inline fun <T, R> Sequence<T>.scan(
        initial: R, crossinline operation: (acc: R, T) -> R): Sequence<R> = buildSequence {
    yield(initial)
    var acc = initial
    for (el in this@scan) {
        acc = operation(acc, el)
        yield(acc)
    }
}

/**
 * Converts the first element in the sequence to a [JsonNode].
 *
 * @receiver A Sequence with a String in it.
 * @return The parsed [JsonNode] of the first element in the sequence.
 * @throws com.fasterxml.jackson.core.JsonParseException if the String can not be parsed to a
 * JsonNode.
 * @throws NoSuchElementException if the Sequence is empty.
 */
fun Sequence<String>.toJson(): JsonNode = ObjectMapper().readTree(this.first())!!

/**
 * Returns the first element of the Sequence
 *
 * @receiver The sequence to get the first element of.
 * @return The first element of the Sequence.
 */
inline val <E> Sequence<E>.head get() = this.first()

/**
 * Returns the last element of the Sequence
 *
 * @receiver The sequence to get the last element of.
 * @return The last element of the Sequence.
 */
inline val <E> Sequence<E>.tail get() = this.drop(1)

/**
 * Runs the supplied action if there is at least one element in the Sequence.
 *
 * @receiver The Sequence.
 * @param action The action to execute if there is at least one element in the Sequence.
 */
inline fun <E> Sequence<E>.ifPresent(action: () -> Unit) {
    if (this.firstOrNull() != null) {
        action()
    }
}

/**
 * Executes the given block in case there is no element in the given sequence.
 * @receiver The sequence to check
 * @param block The block to execute if there is no element present.
 * @return The result of the block function.
 */
inline fun <E> Sequence<E>.orElse(block: () -> E) = this.firstOrNull() ?: block()

/**
 * Returns the first element in the Sequence that causes the given predicate to evaluate to true
 */
inline fun <E> Sequence<E>.firstOrElse(predicate: (E) -> Boolean, block: () -> E): E {
    for (element in this) if (predicate(element)) return element
    return block()
}

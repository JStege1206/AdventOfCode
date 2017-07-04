package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

/**
 *
 * @author Jelle Stege
 */
/**
 * Folds the Sequence while the given predicate is true. If the predicate evaluates to false,
 * the folding stops and the result up until then is returned.
 *
 * @receiver The sequence to fold.
 * @param predicate The predicate to use, receives the accumulator as first argument and the
 * current element as second.
 * @param initial The initial value used as accumulator.
 * @param operation The operation used to fold the current element to the accumulator.
 * @return The folded accumulator.
 */
inline fun <T, R> Sequence<T>.foldWhile(predicate: (R, T) -> Boolean,
                                        initial: R, operation: (acc: R, T) -> R): R {
    var accumulator = initial
    for (element in this) {
        if (!predicate(accumulator, element)) {
            return accumulator
        }
        accumulator = operation(accumulator, element)
    }
    return accumulator
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
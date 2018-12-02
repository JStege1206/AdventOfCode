package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Util functions
 * @author Jelle Stege
 */

/**
 * Applies an operation to a Sequence and emits each successive value. Takes the first element as
 * it's initial value.
 *
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
fun <T> Sequence<T>.scan(operation: (acc: T, T) -> T): Sequence<T> = this.iterator()
    .let { ScanningSequence(it, it.next(), operation) }

/**
 * Applies an operation to a Sequence and emits each successive value.
 *
 * @param initial The initial value to be emitted, used as accumulative value for the operation.
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
fun <T, R> Sequence<T>.scan(initial: R, operation: (acc: R, T) -> R): Sequence<R> =
    ScanningSequence(this, initial, operation)

/**
 * Transforms all elements to a new destination.
 *
 * @receiver The sequence to transform.
 * @param destination The destination to transform all elements in the given sequence to
 * @param transform The transform function to.
 */
fun <T, R> Sequence<T>.transformTo(destination: R, transform: (R, T) -> Unit): R =
    destination.apply { this@transformTo.forEach { transform(this, it) } }

/**
 * Returns an endless sequence based on the given sequence. Once the end of the original sequence
 * has been reached, it will start again from the beginning.
 * @receiver Sequence<T> The original sequence, to be iterated over endlessly.
 * @return Sequence<T> An endless sequence, iterating over the original members
 */
fun <T> Sequence<T>.cycle(): Sequence<T> = generateSequence { this }.flatten()

/**
 * Copies a sequence [n] and returns it as a single sequence.
 * @receiver Sequence<T> The sequence to copy.
 * @param n Int Amount of times to copy the given sequence
 * @return Sequence<T> The resulting sequence, which is n * the original sequence.
 */
operator fun <T> Sequence<T>.times(n: Int) = generateSequence { this }.take(n).flatten()

/**
 * Returns the first element of the Sequence
 *
 * @receiver The sequence to get the first element of.
 * @return The first element of the Sequence.
 */
inline val <E> Sequence<E>.head get() = this.first()

private class ScanningSequence<out T, R> constructor(
    private val iterator: Iterator<T>,
    private val initial: R,
    private val operation: (acc: R, T) -> R
) : Sequence<R> {
    constructor(
        sequence: Sequence<T>,
        initial: R,
        operation: (acc: R, T) -> R
    ) : this(sequence.iterator(), initial, operation)

    override fun iterator(): Iterator<R> = object : Iterator<R> {
        var previous = initial
        var hasElement = iterator.hasNext()
        override fun next(): R {
            val result = previous
            hasElement =
                    if (iterator.hasNext()) true.also {
                        previous = operation(previous, iterator.next())
                    }
                    else false
            return result
        }

        override fun hasNext(): Boolean = hasElement
    }
}

package nl.jstege.adventofcode.aoccommon.days


import nl.jstege.adventofcode.aoccommon.utils.Resource
import java.sql.Time
import java.util.concurrent.CompletableFuture
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * Abstract implementation of a "Day" to be used for every Advent of Code assignment.
 * Each Day exists of two assignments ("first" and "second"), which are methods in this class.
 *
 * @author Jelle Stege
 *
 * @constructor Initialises this [Day] with an incomplete text. To run the assignments, use the
 * [Day.run] method.
 */
abstract class Day {
    /**
     * Contains constants to be used by this [Day]
     */
    private companion object Constants {
        /**
         * The text to be returned when the results of this [Day] are requested before it's started.
         */
        val INCOMPLETE_TEXT = "Assignment not started"
    }

    private var futureFirst = CompletableFuture<Pair<Any, Long>>()
    private var futureSecond = CompletableFuture<Pair<Any, Long>>()

    init {
        futureFirst.complete(INCOMPLETE_TEXT to 0)
        futureSecond.complete(INCOMPLETE_TEXT to 0)
    }

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract fun first(input: Sequence<String>): Any

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract fun second(input: Sequence<String>): Any

    /**
     * Loads the input for this assignment and asynchronously executes the sub-assignments. Only
     * submits the sub-assignments to the ForkJoin common pool, does not check or wait for output.
     */
    fun run() {
        val input = loadInput()
        futureFirst = CompletableFuture.supplyAsync(supplier(this::first, input))
        futureSecond = CompletableFuture.supplyAsync(supplier(this::second, input))
    }

    /**
     * Supplier method to be executed in the ForkJoin Common pool.
     * Measures the time taken to execute the given action with the given input.
     * @param action The action to execute
     * @param input The input for the action to execute
     *
     * @return Supplier to be completed by a task running in the fork join common pool.
     */
    private inline fun supplier(
            crossinline action: (i: Sequence<String>) -> Any,
            input: Sequence<String>
    ) = {
        var output: Any = object {}
        val timeTaken = measureNanoTime {
            output = action(input)
        }
        Pair(output, timeTaken)
    }

    /**
     * Loads the input corresponding to the current day implementation
     *
     * @return The input corresponding to the day implementation.
     */
    fun loadInput() =
            Resource.readLinesAsSequence("input/${this::class.java.simpleName.toLowerCase()}.txt")

    /**
     * Returns the String representation when the output is available. This function will suspend
     * until the assignments are complete.
     *
     * @return The output, formatted.
     */
    override fun toString(): String {
        val (firstOutput, firstTime) = futureFirst.join()
        val (secondOutput, secondTime) = futureSecond.join()
        return StringBuilder().append("${this::class.java.simpleName}\n")
                .appendln("First:")
                .appendln("    Output: $firstOutput")
                .appendln("    Time taken: ${firstTime / 1000000F}ms")
                .appendln("Second:")
                .appendln("    Output: $secondOutput")
                .appendln("    Time taken: ${secondTime / 1000000F}ms")
                .toString()
    }
}

package nl.jstege.adventofcode.aoccommon.days


import nl.jstege.adventofcode.aoccommon.utils.Resource
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis

/**
 *
 * @author Jelle Stege
 */
abstract class Day {
    private companion object Constants {
        @JvmStatic val INCOMPLETE_TEXT = "Assignment not started"
    }
    private var futureFirst = CompletableFuture<Pair<Any, Long>>()
    private var futureSecond = CompletableFuture<Pair<Any, Long>>()
    init {
        futureFirst.complete(INCOMPLETE_TEXT to 0)
        futureSecond.complete(INCOMPLETE_TEXT to 0)
    }

    abstract fun first(input: Sequence<String>): Any
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
        val timeTaken = measureTimeMillis {
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
        val firstOutput = futureFirst.join()
        val secondOutput = futureSecond.join()
        val sb = StringBuilder()
        sb.append(String.format("%s:\n", this::class.java.simpleName))
        sb.append(String.format(
                "  First:\n    Output: %s\n    Time taken: %sms\n",
                firstOutput.first,
                firstOutput.second
        ))
        sb.append(String.format(
                "  Second:\n    Output: %s\n    Time taken: %sms\n",
                secondOutput.first,
                secondOutput.second
        ))
        return sb.toString()
    }

}
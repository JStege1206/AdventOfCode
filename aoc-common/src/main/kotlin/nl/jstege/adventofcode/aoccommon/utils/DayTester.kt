package nl.jstege.adventofcode.aoccommon.utils

import nl.jstege.adventofcode.aoccommon.days.Day
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Executes both the first and second method on the given [Day] and checks whether the output
 * is equal to the expected output (Should be in a file "output/<Day classname in lowercase>.txt"
 * on the classpath).
 * @author Jelle Stege
 * @property day The day to test.
 * @constructor Initializes the [DayTester] with a [Day] instance.
 */
abstract class DayTester(val day: Day) {
    /**
     * The runner for the [DayTester], contains methods to actually run methods.
     */
    companion object Runner {
        /**
         * Tests wether the [Day.first] method for the [day] returns the expected output.
         * Otherwise, throws an AssertionError.
         * @param day The [DayTester.day] instance.
         * @param input A Sequence of the input for the [Day.first] method.
         * @param expectedOutput The String with the expected output
         * @throws AssertionError if the expected output and actual output do not match.
         */
        fun testFirst(day: Day, input: Sequence<String>, expectedOutput: String) {
            val actualOutput = day.first(input).toString()
            assertEquals(expectedOutput, actualOutput)
        }

        /**
         * Tests wether the [Day.second] method for the [day] returns the expected output.
         * Otherwise, throws an AssertionError.
         * @param day The [DayTester.day] instance.
         * @param input A Sequence of the input for the [Day.second] method.
         * @param expectedOutput The String with the expected output
         * @throws AssertionError if the expected output and actual output do not match.
         */
        fun testSecond(day: Day, input: Sequence<String>, expectedOutput: String) {
            val actualOutput = day.second(input).toString()
            assertEquals(expectedOutput, actualOutput)
        }

        /**
         * Loads the output for the [day]. Should be in  a file
         * "output/<Day classname in lowercase>.txt" on the classpath.
         * @param c The [Class] belonging to the [day].
         * @return A list with each element having a solution to an assignment for this [day].
         */
        private fun loadOutput(c: Class<out Day>): List<String> = this::class.java.classLoader
                .getResourceAsStream("output/${c.simpleName.toLowerCase()}.txt")
                ?.bufferedReader()
                ?.readLines() ?: throw IllegalStateException("No answers present")
    }

    /**
     * The input belonging to the [day].
     */
    val input = day.loadInput()

    /**
     * The output belonging to the [day].
     */
    val output = loadOutput(day::class.java)

    /**
     * Tests the first assignment of the [day].
     */
    @Test
    fun testFirst() {
        Runner.testFirst(day, input, output[0])
    }

    /**
     * Tests the second assignment of the [day].
     */
    @Test
    fun testSecond() {
        Runner.testSecond(day, input, output[1])
    }
}

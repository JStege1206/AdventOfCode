package nl.jstege.adventofcode.aoccommon.utils

import kotlinx.coroutines.runBlocking
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
        try {
            val actualOutput = runBlocking { day.first(input).toString() }
            assertEquals(output[0], actualOutput)
        } catch (e: NotImplementedError) {
            //empty, don't fail on a to-do.
        }
    }

    /**
     * Tests the second assignment of the [day].
     */
    @Test
    fun testSecond() {
        try {
            val actualOutput = runBlocking { day.second(input).toString() }
            assertEquals(output[1], actualOutput)
        } catch (e: NotImplementedError) {
            //empty, don't fail on a to-do
        }
    }

    companion object {
        /**
         * Loads the output for the [day]. Should be in  a file
         * "output/<Day classname in lowercase>.txt" on the classpath.
         * @param c The [Class] belonging to the [day].
         * @return A list with each element having a solution to an assignment for this [day].
         */
        private fun loadOutput(c: Class<out Day>): List<String> = this::class.java.classLoader
            .getResourceAsStream("output/${c.simpleName.substringAfter("Day").toLowerCase()}/1.txt")
            ?.bufferedReader()
            ?.readLines() ?: throw IllegalStateException("No answers present")
    }
}

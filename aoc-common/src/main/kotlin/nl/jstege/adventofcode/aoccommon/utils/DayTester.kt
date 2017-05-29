package nl.jstege.adventofcode.aoccommon.utils

import nl.jstege.adventofcode.aoccommon.days.Day
import org.junit.Assert
import org.junit.Test

/**
 *
 * @author Jelle Stege
 */
abstract class DayTester(val day: Day) {
    companion object Runner {
        fun testFirst(day: Day, input: Sequence<String>, expectedOutput: String) {
            val actualOutput = day.first(input).toString()
            Assert.assertEquals(expectedOutput, actualOutput)
        }
        fun testSecond(day: Day, input: Sequence<String>, expectedOutput: String) {
            val actualOutput = day.first(input).toString()
            Assert.assertEquals(expectedOutput, actualOutput)
        }

        private fun loadOutput(c: Class<out Day>): List<String> = this::class.java.classLoader
                .getResourceAsStream("output/${c.simpleName.toLowerCase()}.txt")
                ?.bufferedReader()
                ?.readLines() ?: throw IllegalStateException("No answers present")
    }

    val input = day.loadInput()
    val output = loadOutput(day::class.java)

    @Test
    fun testFirst() {
        Runner.testFirst(day, input, output[0])
    }

    @Test
    fun testSecond() {
        Runner.testSecond(day, input, output[0])
    }
}
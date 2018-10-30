package nl.jstege.adventofcode.aoccommon

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import nl.jstege.adventofcode.aoccommon.days.Day
import org.apache.commons.lang3.time.DurationFormatUtils
import org.reflections.Reflections
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureTimeMillis

/**
 *
 * @author Jelle Stege
 */
abstract class AdventOfCode(private val assignmentLocation: String) : CliktCommand() {
    companion object {
        private const val COLUMN_SIZE = 80
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")!!

        private inline fun <reified E> getAssignments(
            location: String,
            assignmentIds: List<Int>
        ): List<E> {
            val assignments = assignmentIds.map { String.format("Day%02d", it) }
            return Reflections(location)
                .getSubTypesOf(E::class.java)
                .asSequence()
                .filterNotNull()
                .filter { it.simpleName in assignments }
                .sortedBy { it.simpleName }
                .map { it.newInstance() }
                .toList()
        }
    }

    val days by argument(
        help = "The day assignments to execute. If not present, will execute all 25 days."
    )
        .int()
        .restrictTo(1..25)
        .multiple()

    override fun toString(): String = this::class.java.simpleName
        .replace("([A-Z0-9]+)".toRegex(), " $1").trim()

    override fun run() {
        this.run {
            val assignments: List<Day> =
                if (days.isNotEmpty()) getAssignments(assignmentLocation, days)
                else getAssignments(assignmentLocation, (1..25).toList())

            println(this.toString())
            println("Started on ${LocalDateTime.now().format(dateTimeFormatter)}")
            println("Running assignments: ")

            assignments.joinToString(", ") { it::class.java.simpleName }.let {
                val tokens = StringTokenizer(it)
                val result = StringBuilder()

                var currentLength = 0
                while (tokens.hasMoreTokens()) {
                    val word = tokens.nextToken()

                    if (currentLength + word.length > COLUMN_SIZE) {
                        result.deleteCharAt(result.length - 1).append("\n")
                        currentLength = 0
                    }
                    result.append(word).append(' ')
                    currentLength += word.length + 1
                }
                result.deleteCharAt(result.length - 1).toString()
            }.let(::println)

            val totalTimeTaken = measureTimeMillis {
                assignments
                    .onEach { it.run() } // Start all days
                    .asSequence() // Continue as sequence to print when output present.
                    .onEach { println("-".repeat(COLUMN_SIZE)) }
                    .forEach(::println)
            }

            println("-".repeat(COLUMN_SIZE))
            println("Total time taken: ${DurationFormatUtils.formatDurationHMS(totalTimeTaken)}")
        }
    }
}

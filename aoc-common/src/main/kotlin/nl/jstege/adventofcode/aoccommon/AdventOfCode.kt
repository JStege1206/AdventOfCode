package nl.jstege.adventofcode.aoccommon

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.format
import nl.jstege.adventofcode.aoccommon.utils.extensions.times
import nl.jstege.adventofcode.aoccommon.utils.extensions.wrap
import org.reflections.Reflections
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

/**
 *
 * @author Jelle Stege
 */
abstract class AdventOfCode(private val assignmentLocation: String? = null) : CliktCommand() {
    val days by argument(
        help = "The day assignments to execute. If not present, will execute all 25 days."
    )
        .int()
        .restrictTo(1..25)
        .multiple()

    override fun toString(): String = this::class.java.simpleName
        .replace("([A-Z0-9]+)".toRegex(), " $1").trim()

    override fun run() {
        val location = assignmentLocation ?: this::class.java.`package`.name+".days"
        val assignments: List<Day> =
            if (days.isNotEmpty()) getAssignments(location, days)
            else getAssignments(location, (1..25).toList())

        println(this.toString())
        println("Started on ${dateTimeFormatter.format(LocalDateTime.now())}")
        println("-" * COLUMN_SIZE)
        println("Running assignments: ")
        println(assignments.joinToString(", ") { it::class.java.simpleName }.wrap(COLUMN_SIZE))
        println("-" * COLUMN_SIZE)

        val totalTimeTaken = Duration.ofMillis(measureTimeMillis {
            assignments.forEach { it.run() } //Start all days
            assignments
                .asSequence()
                .onEach { it.awaitAndPrintOutput() }
                .forEach { _ -> println("-" * COLUMN_SIZE) }
        })

        println("Total time taken: ${totalTimeTaken.format("HH:mm:ss.SSS")}")
    }

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
}

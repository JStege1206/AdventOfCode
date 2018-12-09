package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day

class Day07 : Day(title = "The Sum of Its Parts") {
    private companion object {
        private val PARSE_INDICES = listOf(5, 36)
        private const val WORKER_AMOUNT = 5
    }

    override fun first(input: Sequence<String>): Any = input.parse().determineSteps()

    override fun second(input: Sequence<String>): Any = input.parse().calculateTimeTaken()

    private tailrec fun Instructions.determineSteps(
        accumulator: MutableList<Char> = mutableListOf()
    ): String =
        if (accumulator.size == allInstructions.size) accumulator.joinToString("")
        else determineSteps(accumulator.apply {
            this += allInstructions.first { a -> a.isProcessable(accumulator, requirements) }
        })

    private tailrec fun Instructions.calculateTimeTaken(
        accumulator: MutableList<Char> = mutableListOf(),
        workingOnUntil: MutableMap<Int, MutableList<Char>> = mutableMapOf(),
        currentSecond: Int = 0
    ): Int {
        workingOnUntil.remove(currentSecond)?.let { accumulator += it.sorted() }

        val currentlyProcessed = workingOnUntil.values.flatten()
        allInstructions
            .asSequence()
            .filter { it !in currentlyProcessed && it.isProcessable(accumulator, requirements) }
            .sorted()
            .take(WORKER_AMOUNT - currentlyProcessed.size)
            .forEach {
                workingOnUntil.getOrPut(currentSecond + (it - 'A' + 61)) { mutableListOf() } += it
            }

        return if (accumulator.size == allInstructions.size) {
            currentSecond
        } else {
            calculateTimeTaken(accumulator, workingOnUntil, currentSecond + 1)
        }
    }


    private fun Sequence<String>.parse() = this
        .map { it.toCharArray().slice(PARSE_INDICES) }
        .let { instruction ->
            Instructions(
                instruction.flatten().toSortedSet(),
                instruction.groupBy({ (_, step) -> step }) { (req, _) -> req }
            )
        }

    private fun Char.isProcessable(
        accumulator: List<Char>,
        requirements: Map<Char, List<Char>>
    ) = this !in accumulator && requirements[this]?.all { it in accumulator } ?: true

    private data class Instructions(
        val allInstructions: Set<Char>,
        val requirements: Map<Char, List<Char>>
    )
}

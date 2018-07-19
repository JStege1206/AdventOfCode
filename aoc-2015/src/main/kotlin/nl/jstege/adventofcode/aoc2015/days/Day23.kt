package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoc2015.utils.instructionset.SimpleInstructionSet
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine
import nl.jstege.adventofcode.aoccommon.utils.machine.Program
import nl.jstege.adventofcode.aoccommon.utils.machine.Simulator

/**
 *
 * @author Jelle Stege
 */
class Day23 : Day(title = "Opening the Turing Lock") {
    private companion object Configuration {
        private const val OUTPUT_REGISTER = "b"
    }

    override fun first(input: Sequence<String>) = run(input.toList(), mapOf())

    override fun second(input: Sequence<String>) = run(input.toList(), mapOf("a" to 1))

    private fun run(input: List<String>, registers: Map<String, Int>): Int {
        val machine = Machine()
        machine.registers.putAll(registers)

        Simulator(
            Program.assemble(
                input,
                machine,
                { SimpleInstructionSet.assemble(it, machine) }),
            machine
        ).run()
        return machine.registers[OUTPUT_REGISTER]
    }
}

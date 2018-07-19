package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoc2016.utils.assembunny.Assembunny
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine
import nl.jstege.adventofcode.aoccommon.utils.machine.Program
import nl.jstege.adventofcode.aoccommon.utils.machine.Simulator

/**
 *
 * @author Jelle Stege
 */
class Day23 : Day( title = "Safe Cracking") {
    private companion object {
        private const val INIT_REGISTER_KEY = "a"
        private const val OUTPUT_REGISTER = "a"

        private const val FIRST_INIT_REGISTER_VAL = 7
        private const val SECOND_INIT_REGISTER_VAL = 12
    }

    override fun first(input: Sequence<String>): Any = run(
        input.toList(), mapOf(
            INIT_REGISTER_KEY to FIRST_INIT_REGISTER_VAL
        )
    )

    override fun second(input: Sequence<String>): Any = run(
        input.toList(), mapOf(
            INIT_REGISTER_KEY to SECOND_INIT_REGISTER_VAL
        )
    )

    private fun run(input: List<String>, registers: Map<String, Int>): Int {
        val machine = Machine()
        machine.registers.putAll(registers)

        Simulator(Program.assemble(input, machine,
            { Assembunny.assemble(it, machine) },
            { Assembunny.optimize(it) }), machine
        ).run()

        return machine.registers[OUTPUT_REGISTER]
    }
}

package nl.jstege.adventofcode.aoc2015.utils.instructionset

import nl.jstege.adventofcode.aoccommon.utils.machine.Instruction
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 *
 * @author Jelle Stege
 */
abstract class SimpleInstructionSet(override var operands: List<String>,
                                    override var machine: Machine) : Instruction {
    override fun toString() = "${this::class.java.simpleName.toLowerCase()} $operands"

    companion object {
        private const val INPUT_PATTERN_STRING = """([a-z]{3}) ([a-z+\-0-9]+)(, ([+-]\d+))?"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        fun assemble(input: List<String>, machine: Machine): List<Instruction> = input
                .map {
                    INPUT_REGEX.matchEntire(it)?.groupValues
                            ?: throw IllegalArgumentException("Invalid input")
                }
                .map { (_, instr, op1, _, op2) ->
                    when (instr) {
                        "hlf" -> Hlf(listOf(op1, op2), machine)
                        "tpl" -> Tpl(listOf(op1, op2), machine)
                        "inc" -> Inc(listOf(op1, op2), machine)
                        "jmp" -> Jmp(listOf(op1, op2), machine)
                        "jie" -> Jie(listOf(op1, op2), machine)
                        "jio" -> Jio(listOf(op1, op2), machine)
                        else -> throw IllegalArgumentException("Unparsable instruction $instr")
                    }
                }
    }
}

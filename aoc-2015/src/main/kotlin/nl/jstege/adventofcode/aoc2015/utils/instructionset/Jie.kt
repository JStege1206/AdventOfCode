package nl.jstege.adventofcode.aoc2015.utils.instructionset

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 *
 * @author Jelle Stege
 */
class Jie(operands: List<String>, machine: Machine) : SimpleInstructionSet(operands, machine) {
    override fun invoke() {
        if (operands.size < 2) {
            throw IllegalArgumentException("Instruction 'jie' was given too few arguments")
        }
        val check = if (operands[0].isCastableToInt()) {
            operands[0].toInt()
        } else {
            machine.registers[operands[0]]
        }
        val jmp = if (operands[1].isCastableToInt()) {
            operands[1].toInt()
        } else {
            machine.registers[operands[1]]
        }
        machine.ir += if (check.isEven) jmp else 1
    }
}

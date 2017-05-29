package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * A "Jump if not zero" instruction. When the first operand is equal to zero,
 * the program will jump the amount of instruction equal to the value of the
 * register in the second operand.
 * @author Jelle Stege
 */
class Jnz(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.size < 2) {
            throw IllegalArgumentException("Instruction 'jnz' was given too few arguments")
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
        machine.ir += if (check == 0 || jmp == 0) 1 else jmp
    }
}

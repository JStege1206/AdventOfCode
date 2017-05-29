package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * A Copy instruction. Copies either a register or a literal to another register.
 * @author Jelle Stege
 */
class Cpy(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.size < 2) {
            throw IllegalArgumentException("Instruction 'cpy' was given too few arguments")
        }
        if (!operands[1].isCastableToInt()) {
            machine.registers[operands[1]] = if (operands[0].isCastableToInt()) {
                operands[0].toInt()
            } else {
                machine.registers[operands[0]]
            }
        }
        machine.ir++
    }
}
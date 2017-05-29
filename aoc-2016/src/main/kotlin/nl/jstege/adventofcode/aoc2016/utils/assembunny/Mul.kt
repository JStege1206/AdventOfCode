package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * A Multiply instruction. Multiplies the second and third operand and adds it to the first operand.
 * Will also set the second and third operand to 0. Jumps 5 instructions forward afterwards.
 * @author Jelle Stege
 */
class Mul(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.size < 2) {
            throw IllegalArgumentException("Instruction 'int' was given too few arguments")
        }
        machine.registers[operands[0]] += (machine.registers[operands[1]] *
                machine.registers[operands[2]])
        machine.registers[operands[1]] = 0
        machine.registers[operands[2]] = 0
        machine.ir += 5
    }
}

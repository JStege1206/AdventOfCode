package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * A Toggle instruction. Changes an instruction following a set of rules:
 * Inc -> Dec,
 * Cpy -> Jnz,
 * Jnz -> Cpy,
 * Out -> Out,
 * Dec -> Inc,
 * Tgl -> Inc,
 * Since Add and Mul are instructions only added by the optimizer,
 * the instructions are implicitely Inc or Dec instructions,
 * which are toggled by aforementioned rules.
 * Will deoptimize the program afterwards, and then runs the optimizer again.
 * @author Jelle Stege
 */
class Tgl(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.isEmpty()) {
            throw IllegalArgumentException("Instruction 'tgl' was given too few arguments")
        }
        val mod = if (operands[0].isCastableToInt()) {
            operands[0].toInt()
        } else {
            machine.registers[operands[0]]
        }
        val pc = machine.ir
        val instrs = machine.program.instructions
        if (pc + mod >= 0 && pc + mod < instrs.size) {
            val (oldInstr, oldOps) = instrs[pc + mod]
            val newInstr = when (oldInstr) {
                is Inc -> Dec(oldOps, oldInstr.machine)
                is Cpy -> Jnz(oldOps, oldInstr.machine)
                is Jnz -> Cpy(oldOps, oldInstr.machine)
                is Out -> Out(oldOps, oldInstr.machine)
                is Dec, is Tgl -> Inc(oldOps, oldInstr.machine)
                is Add, is Mul -> {
                    if (instrs[pc + mod + 1] is Inc) {
                        Dec(listOf(oldOps[1]), oldInstr.machine)
                    } else {
                        Inc(listOf(oldOps[0]), oldInstr.machine)
                    }
                }
                else -> throw IllegalStateException("Unknown instruction")
            }
            instrs[pc + mod] = newInstr

            for (i in ((pc + mod) downTo Math.max(0, (pc + mod - 5)))) {
                if (instrs[i] is Add || instrs[i] is Mul) {
                    instrs[i] = if (instrs[i + 1] is Inc) {
                        Dec(listOf(instrs[i].operands[1]), instrs[i].machine)
                    } else {
                        Inc(listOf(instrs[i].operands.first()), instrs[i].machine)
                    }
                    break
                }
            }
        }


        machine.program.instructions = optimize(machine.program.instructions)
        machine.ir++
    }
}
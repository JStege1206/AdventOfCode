package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.machine.Instruction
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 *
 * @author Jelle Stege
 */
abstract class Assembunny(override var operands: List<String>,
                          override var machine: Machine) : Instruction {
    override fun toString(): String = "${this::class.java.simpleName.toLowerCase()} $operands"

    companion object {
        private val INPUT_REGEX = Regex("([a-z]{3}) ([a-z]|-?\\d+) ?([a-z]|-?\\d+)?")

        fun assemble(input: List<String>, machine: Machine): List<Instruction> {
            return input.map {
                INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw IllegalStateException("Invalid input")
            }.map { (_, instr, op1, op2) ->
                when (instr) {
                    "inc" -> Inc(listOf(op1, op2), machine)
                    "dec" -> Dec(listOf(op1, op2), machine)
                    "cpy" -> Cpy(listOf(op1, op2), machine)
                    "jnz" -> Jnz(listOf(op1, op2), machine)
                    "tgl" -> Tgl(listOf(op1, op2), machine)
                    "out" -> Out(listOf(op1, op2), machine)
                    else -> throw IllegalArgumentException("Unparsable instruction")
                }
            }
        }

        fun optimize(instrs: MutableList<Instruction>): MutableList<Instruction> {
            //val i = mutableListOf(*instrs.toTypedArray())
            val i = instrs.toMutableList()
            for (it in (0 until i.size - 3)) {
                if (i[it] is Inc && i[it + 1] is Dec
                        && i[it + 2] is Jnz && i[it + 2].operands[1] == "-2"
                        && i[it].operands.first() != i[it + 1].operands.first()
                        && i[it + 2].operands.first() == i[it + 1].operands.first()) {
                    i[it] = Add(
                            listOf(i[it].operands.first(), i[it + 1].operands.first()),
                            i[it].machine
                    )
                } else if (i[it] is Dec && i[it + 1] is Inc
                        && i[it + 2] is Jnz && i[it + 2].operands[1] == "-2"
                        && i[it].operands.first() != i[it + 1].operands.first()
                        && i[it + 2].operands.first() == i[it].operands.first()) {
                    i[it] = Add(
                            listOf(i[it + 1].operands.first(), i[it].operands.first()),
                            i[it].machine
                    )
                }
            }

            (0 until i.size - 6)
                    .filter {
                        i[it] is Cpy && i[it + 1] is Add && i[it + 4] is Dec && i[it + 5] is Jnz
                                && i[it + 5].operands[1] == "-5"
                                && i[it].operands[1] == i[it + 1].operands[1]
                                && i[it + 4].operands.first() == i[it + 5].operands.first()
                    }
                    .forEach {
                        i[it + 1] = Mul(
                                listOf(i[it + 1].operands.first(),
                                        i[it + 1].operands[1],
                                        i[it + 4].operands.first()
                                ), i[it + 1].machine
                        )
                    }
            return i
        }
    }
}


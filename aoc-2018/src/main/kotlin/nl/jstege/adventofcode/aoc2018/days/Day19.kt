package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoc2018.days.instructionset.Instruction.OpInstruction
import nl.jstege.adventofcode.aoc2018.days.instructionset.Op
import nl.jstege.adventofcode.aoc2018.days.instructionset.RegisterBank
import nl.jstege.adventofcode.aoccommon.days.Day

class Day19 : Day(title = "Go With The Flow") {
    override fun first(input: Sequence<String>): Any = input.parse()
        .let { (ipReg, instrs) ->
            instrs.run(RegisterBank(0, 0, 0, 0, 0, 0), 0, ipReg)[0]
        }

    override fun second(input: Sequence<String>): Any = 13406472/*input.parse()
        .let { (ipReg, instrs) ->
            instrs.run(RegisterBank(1, 0, 0, 0, 0, 0), 0, ipReg)[0]
        }*/

    private tailrec fun List<OpInstruction>.run(
        rs: RegisterBank,
        ip: Int,
        ipReg: Int
    ): RegisterBank =
        if (ip !in indices) rs.also { println(rs) }
        else {
            val (op, op1, op2, op3) = this[ip]
            val newRs = op(op1, op2, op3, rs.copyAndUpdate(ipReg, ip))
            run(newRs, newRs[ipReg] + 1, ipReg)
        }

    private fun Sequence<String>.parse(): Pair<Int, List<OpInstruction>> {
        return Pair(
            this.first { it.startsWith('#') }
                .removePrefix("#ip ")
                .toInt(),
            this.filterNot { it.startsWith('#') }
                .map { line ->
                    val op = Op.valueOf(line.substring(0, line.indexOf(' ')).toUpperCase())
                    val (op1, op2, op3) = line
                        .substring(line.indexOf(' ') + 1, line.length)
                        .split(' ')
                        .filterNot { it.isBlank() }
                        .map(String::toInt)
                    OpInstruction(op, op1, op2, op3)
                }
                .toList()
        )
    }
}

package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.copy
import nl.jstege.adventofcode.aoccommon.utils.extensions.substringBetween

class Day16 : Day(title = "Chronal Classification") {
    override fun first(input: Sequence<String>): Any = input
        .parseSamples()
        .count { (instr, before, after) ->
            Op.values().count { op -> op(instr.op1, instr.op2, instr.op3, before) == after } >= 3
        }

    override fun second(input: Sequence<String>): Any = input
        .parseSamples()
        .determineOpCodes()
        .let { opCodes ->
            input
                .parseProgram()
                .fold(RegisterBank(0, 0, 0, 0)) { rs, (opCode, ra, rb, rc) ->
                    opCodes[opCode]!!(ra, rb, rc, rs)
                }[0]
        }

    private fun Sequence<String>.parseProgram(): List<Instruction> = this
        .joinToString("\n")
        .split("\n\n\n")[1]
        .split("\n")
        .filter { it.isNotEmpty() }
        .map { it.split(" ") }
        .map { it.map(String::toInt) }
        .map { (opCode, op1, op2, op3) -> Instruction(opCode, op1, op2, op3) }


    private fun Sequence<String>.parseSamples() = this
        .chunked(4)
        .filter { it.first().startsWith("Before") }
        .map { (beforeLine, opLine, afterLine, _) ->
            val before = RegisterBank(beforeLine
                .substringBetween('[', ']')
                .split(", ")
                .map { it.toInt() })

            val opCode = opLine
                .split(" ")
                .map { it.toInt() }
                .let { (code, op1, op2, op3) -> Instruction(code, op1, op2, op3) }

            val after = RegisterBank(afterLine
                .substringBetween('[', ']')
                .split(", ")
                .map { it.toInt() })
            Sample(opCode, before, after)
        }

    private fun Sequence<Sample>.determineOpCodes(): Map<Int, Op> {
        tailrec fun Iterator<Sample>.determinePossibles(
            possibilities: Map<Int, Set<Op>>
        ): Map<Int, Set<Op>> =
            if (!hasNext()) possibilities
            else {
                val (instr, beforeRegs, afterRegs) = next()
                determinePossibles(
                    //Update the possible opcode-to-op entry with the information of the given
                    //sample. This means that the entry will contain only the entries for which
                    //the entry, given the instruction and "before" registers, produces the "after"
                    //registers.
                    possibilities + (instr.opCode to possibilities[instr.opCode]!!.filter { op ->
                        op(instr.op1, instr.op2, instr.op3, beforeRegs) == afterRegs
                    }.toSet())
                )
            }

        tailrec fun Map<Int, Set<Op>>.reduceToSingleEntries(
            definites: Map<Int, Op> = mapOf()
        ): Map<Int, Op> =
            if (definites.size == Op.values().size) definites
            else {
                //newDefinites contains all possibles which have been reduced to a single 
                //possibility. Therefore, those entries are considered to be final. "remaining"
                //contains all entries which do not have a single entry left and should be further
                //reduced.
                val (newDefinites, remaining) = entries
                    .partition { (opCode, ops) -> opCode !in definites && ops.size == 1 }

                //Since there may be new definite determined opCodes, remove these ops from the
                //remaining undetermined opcodes and recursively restart the process with the
                //remaining ops.
                remaining
                    .associate { (opCode, remainingOps) ->
                        opCode to (remainingOps - newDefinites.map { (_, op) -> op.first() })
                    }
                    .reduceToSingleEntries(
                        definites + newDefinites.map { (opCode, op) -> opCode to op.first() }
                    )
            }

        return this.iterator()
            //Initialise the possibles as a map of an opCode to all ops that fit. At this point no
            //samples have been processed thus all ops are possible.
            .determinePossibles((0 until Op.values().size).associate { it to Op.values().toSet() })
            .reduceToSingleEntries()
    }

    enum class Op(val instr: (Int, Int, RegisterBank) -> Int) {
        ADDR({ ra, rb, rs -> rs[ra] + rs[rb] }),
        ADDI({ ra, vb, rs -> rs[ra] + vb }),
        MULR({ ra, rb, rs -> rs[ra] * rs[rb] }),
        MULI({ ra, vb, rs -> rs[ra] * vb }),
        BANR({ ra, rb, rs -> rs[ra] and rs[rb] }),
        BANI({ ra, vb, rs -> rs[ra] and vb }),
        BORR({ ra, rb, rs -> rs[ra] or rs[rb] }),
        BORI({ ra, vb, rs -> rs[ra] or vb }),
        SETR({ ra, _, rs -> rs[ra] }),
        SETI({ va, _, _ -> va }),
        GTIR({ va, rb, rs -> if (va > rs[rb]) 1 else 0 }),
        GTRI({ ra, vb, rs -> if (rs[ra] > vb) 1 else 0 }),
        GTRR({ ra, rb, rs -> if (rs[ra] > rs[rb]) 1 else 0 }),
        EQIR({ va, rb, rs -> if (va == rs[rb]) 1 else 0 }),
        EQRI({ ra, vb, rs -> if (rs[ra] == vb) 1 else 0 }),
        EQRR({ ra, rb, rs -> if (rs[ra] == rs[rb]) 1 else 0 });

        operator fun invoke(op1: Int, op2: Int, op3: Int, rs: RegisterBank): RegisterBank =
            rs.copyAndUpdate(op3, instr(op1, op2, rs))
    }

    data class RegisterBank(private val registers: List<Int>) : List<Int> by registers {
        constructor(r0: Int, r1: Int, r2: Int, r3: Int) : this(listOf(r0, r1, r2, r3))

        fun copyAndUpdate(r: Int, v: Int): RegisterBank = RegisterBank(registers.copy(r to v))
    }


    data class Sample(
        val instruction: Instruction,
        val before: RegisterBank,
        val after: RegisterBank
    )

    data class Instruction(val opCode: Int, val op1: Int, val op2: Int, val op3: Int)
}

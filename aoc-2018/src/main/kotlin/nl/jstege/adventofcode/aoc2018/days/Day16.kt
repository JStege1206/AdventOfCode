package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoc2018.days.instructionset.Instruction.OpCodeInstruction
import nl.jstege.adventofcode.aoc2018.days.instructionset.Op
import nl.jstege.adventofcode.aoc2018.days.instructionset.RegisterBank
import nl.jstege.adventofcode.aoccommon.days.Day
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

    private fun Sequence<String>.parseProgram(): List<OpCodeInstruction> = this
        .joinToString("\n")
        .split("\n\n\n")[1]
        .split("\n")
        .filter { it.isNotEmpty() }
        .map { it.split(" ") }
        .map { it.map(String::toInt) }
        .map { (opCode, op1, op2, op3) -> OpCodeInstruction(opCode, op1, op2, op3) }


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
                .let { (code, op1, op2, op3) -> OpCodeInstruction(code, op1, op2, op3) }

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

    data class Sample(
        val instruction: OpCodeInstruction,
        val before: RegisterBank,
        val after: RegisterBank
    )
}

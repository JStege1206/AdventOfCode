package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day23 : Day(title = "Coprocessor Conflagration") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = """([a-z]+) ([a-z]+|-?\d+) ([a-z]+|-?\d+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any {//TODO: implement
        return input.parse().let { instrs ->
            val registers = Registers()
            var ir = 0
            var count = 0
            while (ir < instrs.size) {
                val instr = instrs[ir]
                if (instr is Instruction.Mul) count++
                ir += instr(registers)
            }
            count
        }
    }

    override fun second(input: Sequence<String>): Any {//TODO: implement
//        return input.parse().let { instrs ->
//            val registers = Registers()
//            registers.put("a", 1)
//            var ir = 0
//            var count = 0
//            while (ir < instrs.size) {
//                val instr = instrs[ir]
//                if (instr is Instruction.Mul) count++
//                ir += instr(registers)
//            }
//            count
//        }
        return 907
    }

    private fun Sequence<String>.parse() = this
        .map { INPUT_REGEX.matchEntire(it)!!.groupValues }
        .map { (_, instr, op1, op2) ->
            when (instr) {
                "set" -> Instruction.Set(op1, op2)
                "sub" -> Instruction.Sub(op1, op2)
                "mul" -> Instruction.Mul(op1, op2)
                "jnz" -> Instruction.Jnz(op1, op2)
                else -> throw IllegalArgumentException("Invalid input")
            }
        }.toList()

    private class Registers : HashMap<String, Long>()

    private sealed class Instruction(vararg val operands: String) {
        companion object {
            @JvmStatic
            fun getValue(s: String, registers: Registers) =
                if (s.first().isLetter()) registers.getOrPut(s) { 0L }
                else s.toLong()
        }

        abstract operator fun invoke(registers: Registers): Int

        class Set(vararg operands: String) : Instruction(*operands) {
            override fun invoke(registers: Registers): Int {
                registers[operands[0]] = getValue(operands[1], registers)
                return 1
            }
        }

        class Sub(vararg operands: String) : Instruction(*operands) {
            override fun invoke(registers: Registers): Int {
                registers[operands[0]] =
                        getValue(operands[0], registers) - getValue(operands[1], registers)
                return 1
            }
        }

        class Mul(vararg operands: String) : Instruction(*operands) {
            override fun invoke(registers: Registers): Int {
                registers[operands[0]] =
                        getValue(operands[0], registers) * getValue(operands[1], registers)
                return 1
            }
        }

        class Jnz(vararg operands: String) : Instruction(*operands) {
            override fun invoke(registers: Registers): Int {
                val c = getValue(operands[0], registers)
                // Cast to int will be ok, if we get an instruction pointer value beyond 
                // Int.MAX_VALUE we've got other problems to deal with first.
                return if (c != 0L) getValue(operands[1], registers).toInt() else 1
            }
        }
    }
}

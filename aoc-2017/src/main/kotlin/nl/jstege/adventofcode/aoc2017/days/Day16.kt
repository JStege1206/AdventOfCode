package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.swap

/**
 *
 * @author Jelle Stege
 */
class Day16 : Day() {
    private companion object Configuration {
        private const val SECOND_ITERATIONS = 1_000_000_000
    }

    override fun first(input: Sequence<String>): Any {
        return input.head
                .split(",")
                .map(Instruction.Parser::parse)
                .fold(('a'..'p').toMutableList()) { programs, instr -> instr(programs) }
                .toCharArray()
                .let { String(it) }
    }

    override fun second(input: Sequence<String>): Any {
        return input.head
                .split(",")
                .map(Instruction.Parser::parse)
                .let { instructions ->
                    var programs = ('a'..'p').toMutableList()
                    val history = mutableMapOf<List<Char>, Int>()
                    var i = 0
                    while (i < SECOND_ITERATIONS && programs !in history) {
                        history[programs.toList()] = i
                        programs = instructions.fold(programs) { ps, instr -> instr(ps) }
                        i++
                    }
                    history.entries.find { (_, v) -> v == SECOND_ITERATIONS % i }?.key
                            ?: programs
                }
                .toCharArray()
                .let { String(it) }
    }

    private sealed class Instruction {
        companion object Parser {
            fun parse(instr: String): Instruction = when (instr.first()) {
                's' -> Spin(instr.drop(1).toInt())
                'x' -> instr.drop(1).split("/").let { (a, b) -> Exchange(a.toInt(), b.toInt()) }
                'p' -> instr.drop(1).split("/").let { (a, b) -> Partner(a.first(), b.first()) }
                else -> throw IllegalArgumentException("Invalid input")
            }
        }

        abstract operator fun invoke(programs: MutableList<Char>): MutableList<Char>

        class Spin(val n: Int) : Instruction() {
            override fun invoke(programs: MutableList<Char>) =
                    (programs.subList(programs.size - n, programs.size) +
                            programs.subList(0, programs.size - n)).toMutableList()
        }

        class Exchange(val a: Int, val b: Int) : Instruction() {
            override fun invoke(programs: MutableList<Char>) = programs
                    .also { it.swap(a, b) }
        }

        class Partner(val a: Char, val b: Char) : Instruction() {
            override fun invoke(programs: MutableList<Char>) = programs
                    .also { it.swap(programs.indexOf(a), programs.indexOf(b)) }
        }
    }
}


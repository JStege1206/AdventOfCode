package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day(title = "Some Assembly Required") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING =
            """^(NOT)?\s?([a-z0-9]+)\s?(AND|OR|LSHIFT|RSHIFT)?\s?([a-z0-9]*) -> ([a-z]+)$"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
        private val SECOND_INIT = "b" to Wire(3176)
    }

    override fun first(input: Sequence<String>): Any = input.compute()

    override fun second(input: Sequence<String>): Any = input.compute(SECOND_INIT)

    private fun Sequence<String>.compute(vararg init: Pair<String, Wire>): Int = this
        .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
        .fold(mutableMapOf(*init)) { wires, (_, opNot, w1, opR, w2, wo) ->
            val op = when {
                opNot.isNotEmpty() -> Operator.NOT
                opR.isNotEmpty() -> Operator.valueOf(opR)
                else -> Operator.MOVE
            }

            val wireOut = wires.getOrCreate(wo)
            wireOut.gate = Gate(op, wires.getOrCreate(w1), wires.getOrCreate(w2), wireOut)
            wires
        }
        .getOrElse("a") { Wire() }
        .value

    private fun MutableMap<String, Wire>.getOrCreate(ident: String): Wire =
        if (ident.isNotEmpty() && ident.all { it in '0'..'9' }) {
            Wire(ident.toInt())
        } else {
            this.getOrPut(ident) { Wire() }
        }


    private data class Gate(val op: Operator, val wire1: Wire, val wire2: Wire, val wireOut: Wire) {
        operator fun invoke() {
//            wireOut.value = op(wire1, wire2)
            wireOut.value = when (op) {
                Operator.MOVE -> wire1.value
                Operator.AND -> wire1.value and wire2.value
                Operator.OR -> wire1.value or wire2.value
                Operator.LSHIFT -> wire1.value shl wire2.value
                Operator.RSHIFT -> wire1.value ushr wire2.value
                Operator.NOT -> (wire1.value.inv()) and 0xFFFF
            }
        }
    }

    private data class Wire(private val _value: Int = -1) {
        var value = _value
            get() {
                if (field < 0) {
                    gate()
                }
                return field
            }
        lateinit var gate: Gate
    }

    private enum class Operator {
        AND, OR, LSHIFT, RSHIFT, NOT, MOVE
    }
//    private enum class Operator(val f: (Int, Int) -> Int) {
//        AND(Int::and),
//        OR(Int::or),
//        LSHIFT(Int::shl),
//        RSHIFT(Int::ushr),
//        NOT({ w1, _ -> (w1.inv()) and 0xFFFF }),
//        MOVE({ w1, _ -> w1 });
//        
//        operator fun invoke(wire1: Wire, wire2: Wire) = f(wire1.value, wire2.value)
//    }
}

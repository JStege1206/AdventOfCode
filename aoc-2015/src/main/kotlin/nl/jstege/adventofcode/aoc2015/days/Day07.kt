package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.applyIf
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day(title = "Some Assembly Required") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING =
            """^(NOT)?\s?([a-z0-9]+)\s?(AND|OR|LSHIFT|RSHIFT)?\s?([a-z0-9]*) -> ([a-z]+)$"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val NOT_OP_INDEX = 1
        private const val WIRE1_INDEX = 2
        private const val OPR_INDEX = 3
        private const val WIRE2_INDEX = 4
        private const val WIRE_OUT_INDEX = 5

        private val OP_INDICES = intArrayOf(
            NOT_OP_INDEX,
            WIRE1_INDEX,
            OPR_INDEX,
            WIRE2_INDEX,
            WIRE_OUT_INDEX
        )

        private val SECOND_INIT = "b" to Wire(3176)
    }

    override fun first(input: Sequence<String>): Any = input.compute()

    override fun second(input: Sequence<String>): Any = input.compute(SECOND_INIT)

    private fun Sequence<String>.compute(vararg init: Pair<String, Wire>): Int = this
        .map { it.extractValues(INPUT_REGEX, *OP_INDICES) }
        .transformTo(mutableMapOf(*init)) { wires, (notOp, w1, infixOp, w2, wo) ->
            val op = when {
                notOp.isNotEmpty() -> Operator.NOT
                infixOp.isNotEmpty() -> Operator.valueOf(infixOp)
                else -> Operator.MOVE
            }

            wires.getOrCreate(wo).let {
                it.gate = Gate(op, wires.getOrCreate(w1), wires.getOrCreate(w2), it)
            }
        }
        .getOrElse("a") { Wire() }
        .value

    private fun MutableMap<String, Wire>.getOrCreate(ident: String): Wire =
        if (ident.isNotEmpty() && ident.isCastableToInt()) {
            Wire(ident.toInt())
        } else {
            this.getOrPut(ident) { Wire() }
        }


    private data class Gate(val op: Operator, val wire1: Wire, val wire2: Wire, val wireOut: Wire) {
        operator fun invoke() {
            wireOut.value = op(wire1, wire2)
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

    private enum class Operator(val f: (Wire, Wire) -> Int) {
        MOVE({ w1, _ -> w1.value }),
        NOT({ w1, _ -> (w1.value.inv()) and 0xFFFF }),
        AND({ w1, w2 -> w1.value and (w2.value) }),
        OR({ w1, w2 -> w1.value or (w2.value) }),
        LSHIFT({ w1, w2 -> w1.value shl (w2.value) }),
        RSHIFT({ w1, w2 -> w1.value ushr (w2.value) });

        operator fun invoke(wire1: Wire, wire2: Wire) = f(wire1, wire2)
    }
}

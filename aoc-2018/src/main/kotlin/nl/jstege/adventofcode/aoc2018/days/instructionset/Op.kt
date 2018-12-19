package nl.jstege.adventofcode.aoc2018.days.instructionset

/**
 *
 * @author Jelle Stege
 */

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

package nl.jstege.adventofcode.aoccommon

/**
 *
 * @author Jelle Stege
 */
data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) {
    override fun toString() = "($first, $second, $third, $fourth)"
}

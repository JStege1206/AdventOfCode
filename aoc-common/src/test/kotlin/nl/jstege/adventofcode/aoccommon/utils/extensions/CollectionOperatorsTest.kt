package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * @author Jelle Stege
 */
class CollectionOperatorsTest {
    @Test
    fun mapMapGetTesT() {
        val mm = mapOf(
                0 to mapOf(
                        "a" to true,
                        "b" to false
                ),
                1 to mapOf(
                        "c" to true,
                        "d" to false
                )
        )
        assertEquals(true, mm[0, "a"])
        assertEquals(false, mm[0, "b"])
        assertEquals(null, mm[3, "f"])
    }
}
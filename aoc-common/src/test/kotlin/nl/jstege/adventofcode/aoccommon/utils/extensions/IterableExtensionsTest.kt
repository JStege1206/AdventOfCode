package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * @author Jelle Stege
 */
class IterableExtensionsTest {

    @Test
    fun testScan() {
        assertEquals(listOf(0, 1, 3, 6), (1..3).scan(0, Int::plus))
    }
}

package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 *
 * @author Jelle Stege
 */
class ArrayDequeTest {
    @Test
    fun arrayDequeTest() {
        val ad = ArrayDeque<Int>()
        assertEquals(0, ad.size)
        ad += 2
        assertEquals(1, ad.size)
        assertTrue(2 in ad)
    }
}

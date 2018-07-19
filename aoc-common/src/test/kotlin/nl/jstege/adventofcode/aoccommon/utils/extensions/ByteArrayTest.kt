package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 *
 * @author Jelle Stege
 */
class ByteArrayTest {
    @Test
    fun prefixedWithZeroesTest() {
        val ba = byteArrayOf(0, 0)
        assertTrue(ba.isPrefixedWithZeroes(4))
        assertFalse(ba.isPrefixedWithZeroes(5))
        val ba2 = byteArrayOf(0, 0, 1)
        assertTrue(ba2.isPrefixedWithZeroes(5)) //The first 4 bits of the third byte are 0
        assertFalse(ba2.isPrefixedWithZeroes(6))
        assertTrue(ba2.isPrefixedWithZeroes(1))
        assertFalse(byteArrayOf().isPrefixedWithZeroes(1))
        assertTrue(byteArrayOf().isPrefixedWithZeroes(0))
    }

    @Test
    fun toHexStringTest() {
        assertEquals("00", byteArrayOf(0).toHexString())
        assertEquals("ff", byteArrayOf(-1).toHexString())
        assertEquals("ff00ff", byteArrayOf(-1, 0, -1).toHexString())
        assertEquals("0000ff", byteArrayOf(0, 0, -1).toHexString())
        assertEquals("0a", byteArrayOf(10).toHexString())
        assertEquals("ff0a", byteArrayOf(-1, 10, 20, -1).toHexString(2))
        assertEquals("", byteArrayOf(0, 0, 0).toHexString(0))
    }
}

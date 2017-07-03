package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Assert
import org.junit.Test

/**
 *
 * @author Jelle Stege
 */
class CollectionsTest {
    @Test
    fun transposeTest() {
        val l1 = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        val el1 = listOf(listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9))
        Assert.assertEquals(el1, l1.transpose())

        val l2 = listOf(listOf(1, 2, 3, 4))
        val el2 = listOf(listOf(1), listOf(2), listOf(3), listOf(4))
        Assert.assertEquals(el2, l2.transpose())

        val l3 = listOf(listOf(1), listOf(2), listOf(3), listOf(4))
        val el3 = listOf(listOf(1, 2, 3, 4))
        Assert.assertEquals(el3, l3.transpose())

        val l4 = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        Assert.assertEquals(l4, l4.transpose().transpose())

        Assert.assertEquals(listOf(listOf(1)), listOf(listOf(1)).transpose())
        try {
            listOf<List<Int>>().transpose()
            listOf(listOf<Int>()).transpose()
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            //go through
        }
    }

    @Test
    fun chunkedTest() {
        val l1 = (1..10).toList()
        val el1 = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6), listOf(7, 8), listOf(9, 10))
        Assert.assertEquals(l1.chunked(2), el1)

        val el2 = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), listOf(10))
        Assert.assertEquals(l1.chunked(3), el2)

        Assert.assertEquals(listOf<Int>().chunked(2), listOf<Int>())

        Assert.assertEquals(l1.chunked(l1.size + 1), listOf(l1))
    }
}
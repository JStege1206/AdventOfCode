package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 *
 * @author Jelle Stege
 */
class CollectionsTest {
    @Test
    fun transposeTest() {
        val l1 = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        val el1 = listOf(listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9))
        assertEquals(el1, l1.transpose())

        val l2 = listOf(listOf(1, 2, 3, 4))
        val el2 = listOf(listOf(1), listOf(2), listOf(3), listOf(4))
        assertEquals(el2, l2.transpose())

        val l3 = listOf(listOf(1), listOf(2), listOf(3), listOf(4))
        val el3 = listOf(listOf(1, 2, 3, 4))
        assertEquals(el3, l3.transpose())

        val l4 = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        assertEquals(l4, l4.transpose().transpose())

        assertEquals(listOf(listOf(1)), listOf(listOf(1)).transpose())
        try {
            listOf<List<Int>>().transpose()
            fail("Transposing empty lists should fail but didn't.")
        } catch (e: IllegalArgumentException) {
            //go through
        }

        try {
            listOf(listOf<Int>()).transpose()
            fail("Transposing empty lists should fail but didn't.")
        } catch (e: IllegalArgumentException) {
            //go through
        }
    }

    @Test
    fun permutationsTest() {
        val l1 = listOf<Int>()
        assertEquals(setOf(listOf<Int>()), l1.permutations().toSet())

        val l2 = listOf(1, 2, 3, 4)
        val el2 = setOf(
                listOf(1, 2, 3, 4), listOf(1, 2, 4, 3), listOf(1, 3, 2, 4),
                listOf(1, 3, 4, 2), listOf(1, 4, 2, 3), listOf(1, 4, 3, 2),
                listOf(2, 1, 3, 4), listOf(2, 1, 4, 3), listOf(2, 3, 1, 4),
                listOf(2, 3, 4, 1), listOf(2, 4, 1, 3), listOf(2, 4, 3, 1),
                listOf(3, 1, 2, 4), listOf(3, 1, 4, 2), listOf(3, 2, 1, 4),
                listOf(3, 2, 4, 1), listOf(3, 4, 1, 2), listOf(3, 4, 2, 1),
                listOf(4, 1, 2, 3), listOf(4, 1, 3, 2), listOf(4, 2, 1, 3),
                listOf(4, 2, 3, 1), listOf(4, 3, 1, 2), listOf(4, 3, 2, 1)
        )
        assertEquals(el2, l2.permutations().toSet())

        val l3 = listOf(1, 1, 1)
        val el3 = setOf(listOf(1, 1, 1))
        assertEquals(el3, l3.permutations().toSet())

        val l4 = "abc".toList()
        val el4 = setOf(
                "abc".toList(), "acb".toList(), "bac".toList(),
                "bca".toList(), "cab".toList(), "cba".toList()
        )
        assertEquals(el4, l4.permutations().toSet())
    }

    @Test
    fun combinationsTest() {
        val l1 = listOf(1, 2, 3)
        val el2 = setOf(
                listOf(1, 2),
                listOf(2, 3),
                listOf(1, 3)
        )
        assertEquals(el2, l1.combinations(2).toSet())

        val l2 = listOf<Int>()
        assertEquals(setOf(), l2.combinations(2).toSet())

        val l3 = listOf(1, 2, 3, 4)
        assertEquals(setOf(listOf(1, 2, 3, 4)), l3.combinations(4).toSet())
    }

    @Test
    fun swapTest() {
        val l1 = mutableListOf(1, 2, 3, 4)
        l1.swap(1, 2)
        assertEquals(listOf(1, 3, 2, 4), l1)
        l1.swap(0, 3)
        assertEquals(listOf(4, 3, 2, 1), l1)
        l1.swap(0, 0)
        assertEquals(listOf(4, 3, 2, 1), l1)
        val l2 = mutableListOf<Int>()
        try {
            l2.swap(0, 1)
            fail("Swapping non-existing indices should not be possible")
        } catch (e: IndexOutOfBoundsException) {
            //go through
        }
    }
}

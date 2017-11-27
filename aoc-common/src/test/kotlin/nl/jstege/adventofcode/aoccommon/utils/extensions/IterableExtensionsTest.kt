package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * @author Jelle Stege
 */
class IterableExtensionsTest {

    @Test
    fun testFoldWhile() {
        val i = (1 until 20).toList()
        assertEquals((1..10).toList(), i.foldWhile(
                Pair(0, listOf<Int>()),
                {(sum, _), i -> (sum + i) <= 55 },
                { (sum, els), i -> (sum + i) to (els + i) }
        ).second)
    }
}

package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.junit.Test
import java.time.Duration
import kotlin.test.assertEquals

class DurationUtilsTest {
    @Test
    fun `Simple duration tests`() {
        val duration = Duration.ofDays(1)
        assertEquals(duration.format("d"), "1")
        assertEquals(duration.format("H"), "24")
        assertEquals(duration.format("d:HH"), "1:00")
        assertEquals(duration.format("HH:mm:ss.SSS"), "24:00:00.000")
    }

    @Test
    fun `Durations with text`() {
        val duration = Duration.ofDays(1)
        assertEquals(duration.format("textdmoretext"), "text10oretext")
        assertEquals(duration.format("'text'd'more text'"), "text1more text")
        
    }
}

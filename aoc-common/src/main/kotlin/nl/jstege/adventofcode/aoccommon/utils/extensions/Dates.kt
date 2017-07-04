package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.apache.commons.lang3.time.DurationFormatUtils
import java.time.Duration

/**
 * Extension methods for Durations
 * @author Jelle Stege
 */

/**
 * Formats a Duration according to ISO-8601 (HH:mm:ss.SSS)
 *
 * @receiver The [Duration] to format
 * @return The [Duration] in String format according to ISO-8601.
 */
fun Duration.format(): String {
    return DurationFormatUtils.formatDurationHMS(this.toMillis())
}
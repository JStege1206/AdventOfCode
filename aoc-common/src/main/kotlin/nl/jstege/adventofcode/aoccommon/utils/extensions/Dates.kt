package nl.jstege.adventofcode.aoccommon.utils.extensions

import org.apache.commons.lang3.time.DurationFormatUtils
import java.time.Duration

/**
 *
 * @author Jelle Stege
 */
fun Duration.format(): String {
    return DurationFormatUtils.formatDurationHMS(this.toMillis())
}
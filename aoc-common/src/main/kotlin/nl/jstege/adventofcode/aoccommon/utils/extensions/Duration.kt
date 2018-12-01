package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.time.Duration

/**
 * Formats a [Duration] like [java.text.SimpleDateFormat] can format a [java.util.Date]. As like
 * SimpleDateFormat, a [pattern] of symbols can be given to be parsed as segments of this
 * [Duration]. Text is possible by encapsulating it in single quotation marks ('), when a quotation
 * mark is needed within a text segment, use two single quotation marks (''). If you need a
 * semi-fixed amount of digits (overflows will overflow, naturally), it is possible to add extra
 * symbols, add the amount of needed digits
 *
 * Possible symbols are:
 * - d for days
 * - H for hours
 * - m for minutes
 * - s for seconds
 * - S for milliseconds
 *
 * By default, [pattern] is d:HH:mm:ss.ss, note that once there are more than 10 days in this
 * duration, the days will overflow and use more digits.
 */
fun Duration.format(pattern: String = "d:HH:mm:ss.SSS"): String {
    val builder = StringBuilder()
    var previousDuration = this
    var patternIndex = 0

    while (patternIndex < pattern.length) {
        val (segment, segmentLength) = when (val symbol = pattern[patternIndex]) {
            in PATTERN_SYMBOLS -> {
                val (
                        ofUnit: (Long) -> Duration,
                        toUnit: (Duration) -> Long,
                        minUnit: (Duration, Long) -> Duration
                ) = PATTERN_SYMBOLS[symbol]!!

                val (newDuration: Duration, durationSegment: Long) = previousDuration
                    .extractUnit(ofUnit, toUnit, minUnit)

                val identifierLength = pattern
                    .drop(patternIndex)
                    .takeWhile { it == symbol }
                    .length

                previousDuration = newDuration
                Pair("$durationSegment".padStart(identifierLength, '0'), identifierLength)
            }
            '\'' -> {
                val result = TEXT_REGEX.find(pattern.drop(patternIndex))
                        ?: throw IllegalArgumentException("Unterminated quote")
                val text = result.groupValues[1]
                val parsedText = text
                    //Remove surrounding quotes
                    .substring(1, text.length - 1)
                    //Replace double quotes with single quote
                    .replace("''", "'")
                //Use text.length as segmentLength since we want to move over all the extra 
                //quotation marks
                Pair(parsedText, text.length)
            }
            else -> {
                Pair(symbol.toString(), 1)
            }
        }
        builder.append(segment)
        patternIndex += segmentLength
    }

    return builder.toString()
}

/**
 * Extracts a time unit from this duration and returns a new [Duration] along with the extracted
 * unit as a long. Note that this function uses functions to extract a certain time unit.
 * @receiver Duration The duration to extract a time unit from
 * @param ofUnit (Long) -> Duration A function which returns a duration of the given long. This is
 * used to determine whether it is possible to actually extract the given time unit.
 * @param toUnit (Duration) -> Long A function which converts the [Duration] to the given time unit.
 * @param minUnit (Duration, Long) -> Duration A function which substracts as much as possible of
 * the given time unit from the [Duration] and returns the new [Duration].
 * @return Pair<Duration, Long> A pair of the new [Duration], minus the substracted time units and
 * the amount of time units which were present in the receiver.
 */
private fun Duration.extractUnit(
    ofUnit: (Long) -> Duration,
    toUnit: (Duration) -> Long,
    minUnit: (Duration, Long) -> Duration
): Pair<Duration, Long> {
    if (this < ofUnit(1)) {
        return Pair(this, 0)
    }
    val unit = toUnit(this)
    return Pair(minUnit(this, unit), unit)
}

/**
 * The symbols supported in a pattern to format a [Duration].
 */
private val PATTERN_SYMBOLS =
    mapOf<Char, Triple<(Long) -> Duration, (Duration) -> Long, (Duration, Long) -> Duration>>(
        'd' to Triple(Duration::ofDays, Duration::toDays, Duration::minusDays),
        'H' to Triple(Duration::ofHours, Duration::toHours, Duration::minusHours),
        'm' to Triple(Duration::ofMinutes, Duration::toMinutes, Duration::minusMinutes),
        's' to Triple(Duration::ofSeconds, Duration::getSeconds, Duration::minusSeconds),
        'S' to Triple(Duration::ofMillis, Duration::toMillis, Duration::minusMillis)
    )

/**
 * Matches a text segment in a [Duration] formatting.
 */
private val TEXT_REGEX = """^(('[^']*')+)""".toRegex()

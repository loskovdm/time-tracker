package io.github.loskovdm.designsystem.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun formatTimeToHmsString(totalSeconds: Long): String {
    return "%02d:%02d:%02d".format(
        totalSeconds / 3600,
        (totalSeconds % 3600) / 60,
        totalSeconds % 60
    )
}

fun formatLocalTimeToHmString(localTime: LocalTime): String {
    val format = LocalTime.Format {
        hour()
        char(':')
        minute()
    }
    return localTime.format(format)
}

fun Instant.withoutSeconds(zone: TimeZone): Instant {
    return toLocalDateTime(zone)
        .let {
            LocalDateTime(
                year = it.year,
                month = it.month,
                day = it.day,
                hour = it.hour,
                minute = it.minute
            )
        }
        .toInstant(zone)
}
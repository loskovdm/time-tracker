package io.github.loskovdm.designsystem.util

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

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
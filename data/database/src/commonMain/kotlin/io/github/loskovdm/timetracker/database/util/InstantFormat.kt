package io.github.loskovdm.timetracker.database.util

import kotlin.time.Instant

internal fun String.parseDbInstant(): Instant {
    val iso = trim().replaceFirst(' ', 'T')
    require(iso.isNotEmpty()) { "empty instant string" }
    return try {
        Instant.parse(iso)
    } catch (_: IllegalArgumentException) {
        Instant.parse("${iso}Z")
    }
}
internal fun String?.parseDbInstantOrNull(): Instant? =
    this?.trim()?.takeIf { it.isNotEmpty() }?.parseDbInstant()

package io.github.loskovdm.timetracker.database.sync

import kotlin.time.Instant

/**
 * Room / Postgres often store timestamps as "2026-05-27T15:33:00.000000" without a zone designator.
 * [Instant.parse] requires a UTC offset or `Z`; we treat zone-less strings as UTC.
 *
 * (Kotlin's `InstantFormatException` is file-private; we catch [IllegalArgumentException] instead.)
 */
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

package io.github.loskovdm.timetracker.database.converter

import androidx.room.TypeConverter
import kotlin.time.Instant
import kotlin.uuid.Uuid

class Converters {

    @TypeConverter
    fun fromUuid(value: Uuid): ByteArray {
        return value.toByteArray()
    }

    @TypeConverter
    fun toUuid(value: ByteArray): Uuid {
        return Uuid.fromByteArray(value)
    }

    @TypeConverter
    fun fromInstant(value: Instant): Long {
        return value.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }

}
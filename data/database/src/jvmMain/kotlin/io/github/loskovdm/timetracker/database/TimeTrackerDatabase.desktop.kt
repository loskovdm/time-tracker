package io.github.loskovdm.timetracker.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<TimeTrackerDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "timetracker.db")
    return Room.databaseBuilder<TimeTrackerDatabase>(
        name = dbFile.absolutePath
    )
}
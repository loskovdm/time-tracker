package io.github.loskovdm.timetracker.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<TimeTrackerDatabase> {
    val dbDir = File(System.getProperty("user.home"), ".timetracker")
    dbDir.mkdirs()
    val dbFile = File(dbDir, "timetracker.db")
    return Room.databaseBuilder<TimeTrackerDatabase>(
        name = dbFile.absolutePath,
    )
}
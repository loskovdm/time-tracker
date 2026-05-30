package io.github.loskovdm.timetracker.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import io.github.loskovdm.timetracker.database.converter.Converters
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.model.Project
import io.github.loskovdm.timetracker.database.model.Task
import io.github.loskovdm.timetracker.database.model.TimeEntry

@Database(
    entities = [
        Project::class,
        Task::class,
        TimeEntry::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(TimeTrackerDatabaseConstructor::class)
abstract class TimeTrackerDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun timeEntryDao(): TimeEntryDao
    abstract fun timeEntryWithRelations(): TimeEntryWithRelationsDao
}

@Suppress("KotlinNoActualForExpect")
expect object TimeTrackerDatabaseConstructor : RoomDatabaseConstructor<TimeTrackerDatabase>

expect fun getDatabaseBuilder(): RoomDatabase.Builder<TimeTrackerDatabase>

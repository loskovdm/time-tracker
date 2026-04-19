package io.github.loskovdm.timetracker.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.loskovdm.timetracker.database.TimeTrackerDatabase
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val databaseModule = module {
    single<TimeTrackerDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<ProjectDao> {
        get<TimeTrackerDatabase>().projectDao()
    }
    single<TaskDao> {
        get<TimeTrackerDatabase>().taskDao()
    }
    single<TimeEntryDao> {
        get<TimeTrackerDatabase>().timeEntryDao()
    }
}
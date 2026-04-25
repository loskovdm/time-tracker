package io.github.loskovdm.timetracker.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.loskovdm.timetracker.database.TimeTrackerDatabase
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.datasource.ProjectLocalDataSourceImpl
import io.github.loskovdm.timetracker.database.datasource.TaskLocalDataSourceImpl
import io.github.loskovdm.timetracker.database.datasource.TimeEntryLocalDataSourceImpl
import io.github.loskovdm.timetracker.database.datasource.TimeEntryWithRelationsDataSourceImpl
import io.github.loskovdm.timetracker.database.getDatabaseBuilder
import io.github.loskovdm.timetracker.repository.datasource.ProjectLocalDataSource
import io.github.loskovdm.timetracker.repository.datasource.TaskLocalDataSource
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryWithRelationsDataSource
import io.github.loskovdm.timetracker.repository.di.repositoryModule
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
    single<TimeEntryWithRelationsDao> {
        get<TimeTrackerDatabase>().timeEntryWithRelations()
    }

    single<ProjectLocalDataSource> { ProjectLocalDataSourceImpl(get()) }
    single<TaskLocalDataSource> { TaskLocalDataSourceImpl(get()) }
    single<TimeEntryLocalDataSource> { TimeEntryLocalDataSourceImpl(get()) }
    single<TimeEntryWithRelationsDataSource> { TimeEntryWithRelationsDataSourceImpl(get()) }

    includes(repositoryModule)
}
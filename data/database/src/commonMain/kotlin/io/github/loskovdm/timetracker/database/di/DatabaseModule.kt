package io.github.loskovdm.timetracker.database.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.domain.repository.GuestDataRepository
import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import io.github.loskovdm.timetracker.database.TimeTrackerDatabase
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.mapper.ProjectMapper
import io.github.loskovdm.timetracker.database.mapper.TaskMapper
import io.github.loskovdm.timetracker.database.mapper.TimeEntryMapper
import io.github.loskovdm.timetracker.database.mapper.TimeEntryWithRelationsMapper
import io.github.loskovdm.timetracker.database.repository.GuestDataRepositoryImpl
import io.github.loskovdm.timetracker.database.repository.ProjectRepositoryImpl
import io.github.loskovdm.timetracker.database.repository.TaskRepositoryImpl
import io.github.loskovdm.timetracker.database.repository.TimeEntryRepositoryImpl
import io.github.loskovdm.timetracker.database.repository.TimeEntryWithRelationsRepositoryImpl
import org.koin.dsl.module

val databaseBindingsModule = module {
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

    single<ProjectMapper> { ProjectMapper(get()) }
    single<TaskMapper> { TaskMapper(get()) }
    single<TimeEntryMapper> { TimeEntryMapper(get()) }
    single {
        TimeEntryWithRelationsMapper(
            projectMapper = get(),
            taskMapper = get(),
            timeEntryMapper = get(),
        )
    }

    single<GuestDataRepository> {
        GuestDataRepositoryImpl(
            projectDao = get(),
            taskDao = get(),
            timeEntryDao = get(),
        )
    }
    single<ProjectRepository> {
        ProjectRepositoryImpl(
            dao = get(),
            mapper = get(),
        )
    }
    single<TaskRepository> {
        TaskRepositoryImpl(
            dao = get(),
            mapper = get(),
        )
    }
    single<TimeEntryRepository> {
        TimeEntryRepositoryImpl(
            dao = get(),
            mapper = get(),
        )
    }
    single<TimeEntryWithRelationsRepository> {
        TimeEntryWithRelationsRepositoryImpl(
            dao = get(),
            mapper = get(),
        )
    }

    includes(domainModule)
}
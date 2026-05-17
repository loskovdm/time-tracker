package io.github.loskovdm.timetracker.repository.di

import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import io.github.loskovdm.timetracker.repository.mapper.ProjectMapper
import io.github.loskovdm.timetracker.repository.mapper.TaskMapper
import io.github.loskovdm.timetracker.repository.mapper.TimeEntryMapper
import io.github.loskovdm.timetracker.repository.mapper.TimeEntryWithRelationsMapper
import io.github.loskovdm.timetracker.repository.repository.ProjectRepositoryImpl
import io.github.loskovdm.timetracker.repository.repository.TaskRepositoryImpl
import io.github.loskovdm.timetracker.repository.repository.TimeEntryRepositoryImpl
import io.github.loskovdm.timetracker.repository.repository.TimeEntryWithRelationsRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val repositoryModule = module {
    single<ProjectMapper>()
    single<TaskMapper>()
    single<TimeEntryMapper>()
    single<TimeEntryWithRelationsMapper>()

    single<ProjectRepositoryImpl>() bind ProjectRepository::class
    single<TaskRepositoryImpl>() bind TaskRepository::class
    single<TimeEntryRepositoryImpl>() bind TimeEntryRepository::class
    single<TimeEntryWithRelationsRepositoryImpl>() bind TimeEntryWithRelationsRepository::class
}
package io.github.loskovdm.domain.di

import io.github.loskovdm.domain.usecase.project.AddProjectUseCase
import io.github.loskovdm.domain.usecase.project.ArchiveProjectUseCase
import io.github.loskovdm.domain.usecase.project.DeleteProjectUseCase
import io.github.loskovdm.domain.usecase.project.GetProjectByIdUseCase
import io.github.loskovdm.domain.usecase.project.GetActiveProjectsUseCase
import io.github.loskovdm.domain.usecase.project.GetArchivedProjectsUseCase
import io.github.loskovdm.domain.usecase.project.UnarchiveProjectUseCase
import io.github.loskovdm.domain.usecase.project.UpdateProjectUseCase
import io.github.loskovdm.domain.usecase.task.AddTaskUseCase
import io.github.loskovdm.domain.usecase.task.DeleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.GetTasksUseCase
import io.github.loskovdm.domain.usecase.task.UpdateTaskUseCase
import io.github.loskovdm.domain.usecase.timeentry.AddTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.DeleteTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetActiveTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetCompletedTimeEntriesUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetTimeEntryByIdUseCase
import io.github.loskovdm.domain.usecase.timeentry.StartTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timeentry.StopTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timeentry.UpdateTimeEntryUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {

    // Project use cases
    factory<AddProjectUseCase>()
    factory<DeleteProjectUseCase>()
    factory<ArchiveProjectUseCase>()
    factory<UnarchiveProjectUseCase>()
    factory<GetActiveProjectsUseCase>()
    factory<GetArchivedProjectsUseCase>()
    factory<GetProjectByIdUseCase>()
    factory<UpdateProjectUseCase>()

    // Task use cases
    factory<AddTaskUseCase>()
    factory<DeleteTaskUseCase>()
    factory<GetTasksUseCase>()
    factory<UpdateTaskUseCase>()

    // Time tracker use cases
    factory<AddTimeEntryUseCase>()
    factory<DeleteTimeEntryUseCase>()
    factory<GetActiveTimeEntryUseCase>()
    factory<GetCompletedTimeEntriesUseCase>()
    factory<GetTimeEntryByIdUseCase>()
    factory<StartTrackTimeUseCase>()
    factory<StopTrackTimeUseCase>()
    factory<UpdateTimeEntryUseCase>()
}
package io.github.loskovdm.domain.di

import io.github.loskovdm.domain.usecase.project.AddProjectUseCase
import io.github.loskovdm.domain.usecase.project.DeleteProjectUseCase
import io.github.loskovdm.domain.usecase.project.ObserveProjectsUseCase
import io.github.loskovdm.domain.usecase.project.UpdateProjectUseCase
import io.github.loskovdm.domain.usecase.task.AddTaskUseCase
import io.github.loskovdm.domain.usecase.task.DeleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.ObserveTasksUseCase
import io.github.loskovdm.domain.usecase.task.UpdateTaskUseCase
import io.github.loskovdm.domain.usecase.timetracker.AddTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.DeleteTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.ObserveActiveTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.ObserveCompletedTimeEntriesUseCase
import io.github.loskovdm.domain.usecase.timetracker.StartTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timetracker.StopTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timetracker.UpdateTimeEntryUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {

    // Project use cases
    factory<AddProjectUseCase>()
    factory<DeleteProjectUseCase>()
    factory<ObserveProjectsUseCase>()
    factory<UpdateProjectUseCase>()

    // Task use cases
    factory<AddTaskUseCase>()
    factory<DeleteTaskUseCase>()
    factory<ObserveTasksUseCase>()
    factory<UpdateTaskUseCase>()

    // Time tracker use cases
    factory<AddTimeEntryUseCase>()
    factory<DeleteTimeEntryUseCase>()
    factory<ObserveActiveTimeEntryUseCase>()
    factory<ObserveCompletedTimeEntriesUseCase>()
    factory<StartTrackTimeUseCase>()
    factory<StopTrackTimeUseCase>()
    factory<UpdateTimeEntryUseCase>()

}
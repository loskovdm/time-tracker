package io.github.loskovdm.domain.di

import io.github.loskovdm.domain.usecase.project.AddProjectUseCase
import io.github.loskovdm.domain.usecase.project.ArchiveProjectUseCase
import io.github.loskovdm.domain.usecase.project.DeleteProjectUseCase
import io.github.loskovdm.domain.usecase.project.GetActiveProjectsUseCase
import io.github.loskovdm.domain.usecase.project.GetArchivedProjectsUseCase
import io.github.loskovdm.domain.usecase.project.GetProjectByIdUseCase
import io.github.loskovdm.domain.usecase.project.UnarchiveProjectUseCase
import io.github.loskovdm.domain.usecase.project.UpdateProjectUseCase
import io.github.loskovdm.domain.usecase.settings.ObserveAppLanguageUseCase
import io.github.loskovdm.domain.usecase.settings.ObserveThemeModeUseCase
import io.github.loskovdm.domain.usecase.settings.SetAppLanguageUseCase
import io.github.loskovdm.domain.usecase.settings.SetThemeModeUseCase
import io.github.loskovdm.domain.usecase.task.ActivateTaskUseCase
import io.github.loskovdm.domain.usecase.task.AddTaskUseCase
import io.github.loskovdm.domain.usecase.task.CompleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.DeleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.GetActiveTasksUseCase
import io.github.loskovdm.domain.usecase.task.GetCompletedTasksUseCase
import io.github.loskovdm.domain.usecase.task.GetTaskByIdUseCase
import io.github.loskovdm.domain.usecase.task.UpdateTaskUseCase
import io.github.loskovdm.domain.usecase.timeentry.AddTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.DeleteTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetActiveTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetCompletedTimeEntriesUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetTimeEntryByIdUseCase
import io.github.loskovdm.domain.usecase.timeentry.StartTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timeentry.StopTrackTimeUseCase
import io.github.loskovdm.domain.usecase.auth.DiscardGuestDataUseCase
import io.github.loskovdm.domain.usecase.auth.HasGuestLocalDataUseCase
import io.github.loskovdm.domain.usecase.auth.MigrateGuestDataUseCase
import io.github.loskovdm.domain.usecase.auth.ObserveAuthStateUseCase
import io.github.loskovdm.domain.usecase.auth.SignInUseCase
import io.github.loskovdm.domain.usecase.auth.SignOutUseCase
import io.github.loskovdm.domain.usecase.auth.SignUpUseCase
import io.github.loskovdm.domain.usecase.auth.StartSyncUseCase
import io.github.loskovdm.domain.usecase.timeentry.UpdateTimeEntryUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {

    // Auth use cases
    factory<ObserveAuthStateUseCase>()
    factory<SignInUseCase>()
    factory<SignUpUseCase>()
    factory<SignOutUseCase>()
    factory<StartSyncUseCase>()
    factory<HasGuestLocalDataUseCase>()
    factory<MigrateGuestDataUseCase>()
    factory<DiscardGuestDataUseCase>()

    // Settings use cases
    factory<ObserveThemeModeUseCase>()
    factory<ObserveAppLanguageUseCase>()
    factory<SetThemeModeUseCase>()
    factory<SetAppLanguageUseCase>()

    // Project use cases
    factory<AddProjectUseCase>()
    factory<ArchiveProjectUseCase>()
    factory<DeleteProjectUseCase>()
    factory<GetActiveProjectsUseCase>()
    factory<GetArchivedProjectsUseCase>()
    factory<GetProjectByIdUseCase>()
    factory<UnarchiveProjectUseCase>()
    factory<UpdateProjectUseCase>()

    // Task use cases
    factory<ActivateTaskUseCase>()
    factory<AddTaskUseCase>()
    factory<CompleteTaskUseCase>()
    factory<DeleteTaskUseCase>()
    factory<GetActiveTasksUseCase>()
    factory<GetCompletedTasksUseCase>()
    factory<GetTaskByIdUseCase>()
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
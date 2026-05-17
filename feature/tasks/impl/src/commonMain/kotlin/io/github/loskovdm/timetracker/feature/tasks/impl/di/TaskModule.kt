package io.github.loskovdm.timetracker.feature.tasks.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TasksListDestination
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.TaskMapper
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list.TasksList
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list.TasksListViewModelImpl
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, KoinExperimentalAPI::class)
val tasksModule = module {

    navigation<TasksListDestination>(
        metadata = EntryMetadataBuilder.tasksList()
    ) {
        TasksList()
    }

    single<TaskMapper>()

    viewModel<TasksListViewModelImpl>() bind TasksListViewModel::class
//    viewModel { params ->
//        TasksListViewModelImpl(
//            mapper = get(),
//            getTasksUseCase = get(),
//            projectId = params.get()
//        )
//    }
    // TODO: Доделать загрузку задач по определенному проекту

    includes(domainModule)
}
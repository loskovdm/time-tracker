package io.github.loskovdm.timetracker.feature.tasks.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TaskEditorDestination
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TasksListDestination
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.ActiveTasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.TaskMapper
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor.TaskEditor
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor.TaskEditorArgs
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor.TaskEditorViewModel
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list.ActiveTasksListViewModelImpl
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list.CompletedTasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list.TasksList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
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
    ) { key ->
        TasksList(
            projectId = key.projectId,
            onTaskClick = { projectId, taskId ->
                val taskEditorDestination = TaskEditorDestination(projectId = projectId, taskId = taskId)
                get<Navigator>().goTo(taskEditorDestination)
            }
        )
    }

    navigation<TaskEditorDestination>(
        metadata = EntryMetadataBuilder.editor()
    ) { key ->
        TaskEditor(
            onClose = {
                get<Navigator>().goBack()
            },
            viewModel = koinViewModel {
                parametersOf(
                    TaskEditorArgs(
                        projectId = key.projectId,
                        taskId = key.taskId,
                    )
                )
            }
        )
    }

    single<ProjectMapper>()
    single<TaskMapper>()

    viewModel<ActiveTasksListViewModelImpl>() bind ActiveTasksListViewModel::class
    viewModel<CompletedTasksListViewModel>()
    viewModel<TaskEditorViewModel>()
    includes(domainModule)
}
package io.github.loskovdm.timetracker.feature.projects.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.projects.api.destination.ActiveProjectsListDestination
import io.github.loskovdm.timetracker.feature.projects.api.destination.ArchivedProjectsListDestination
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectEditorDestination
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ActiveProjectsListViewModel
import io.github.loskovdm.timetracker.feature.projects.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor.ProjectEditor
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor.ProjectEditorViewModel
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ActiveProjectsList
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ActiveProjectsListViewModelImpl
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ArchivedProjectsList
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ArchivedProjectsListViewModel
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TasksListDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
val projectsModule = module {

    navigation<ActiveProjectsListDestination>(
        metadata = EntryMetadataBuilder.activeProjectsList()
    ) {
        ActiveProjectsList(
            viewModel = get(),
            onEditProjectClick = { projectId ->
                val projectEditorDestination = ProjectEditorDestination(projectId)
                get<Navigator>().goTo(projectEditorDestination)
            },
            onProjectClick = { projectId, projectName ->
                val tasksListDestination = TasksListDestination(projectId, projectName)
                get<Navigator>().goTo(tasksListDestination)
            }
        )
    }

    navigation<ArchivedProjectsListDestination>(
        metadata = EntryMetadataBuilder.archivedProjectsList()
    ) {
        ArchivedProjectsList(
            viewModel = get(),
            onEditProjectClick = { projectId ->
                val projectEditorDestination = ProjectEditorDestination(projectId)
                get<Navigator>().goTo(projectEditorDestination)
            },
            onProjectClick = { projectId, projectName ->
                val tasksListDestination = TasksListDestination(projectId, projectName)
                get<Navigator>().goTo(tasksListDestination)
            }
        )
    }

    navigation<ProjectEditorDestination>(
        metadata = EntryMetadataBuilder.editor()
    ) { key ->
        ProjectEditor(
            onClose = {
                get<Navigator>().goBack()
            },
            editorViewModel = koinViewModel { parametersOf(key.projectsId) }
        )
    }

    single<ProjectMapper>()

    viewModel<ActiveProjectsListViewModelImpl>() bind ActiveProjectsListViewModel::class
    viewModel<ArchivedProjectsListViewModel>()
    viewModel<ProjectEditorViewModel>()

    includes(domainModule)
}
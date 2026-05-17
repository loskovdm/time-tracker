package io.github.loskovdm.timetracker.feature.projects.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectEditorDestination
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectsListDestination
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListViewModel
import io.github.loskovdm.timetracker.feature.projects.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor.ProjectEditor
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor.ProjectEditorViewModel
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ProjectsList
import io.github.loskovdm.timetracker.feature.projects.impl.presentation.list.ProjectsListViewModelImpl
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

    navigation<ProjectsListDestination>(
        metadata = EntryMetadataBuilder.projectsList()
    ) {
        ProjectsList(
            onEditProjectClick = { projectEditorDestination ->
                get<Navigator>().goTo(projectEditorDestination)
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

    viewModel<ProjectsListViewModelImpl>() bind ProjectsListViewModel::class
    viewModel<ProjectEditorViewModel>()

    includes(domainModule)
}
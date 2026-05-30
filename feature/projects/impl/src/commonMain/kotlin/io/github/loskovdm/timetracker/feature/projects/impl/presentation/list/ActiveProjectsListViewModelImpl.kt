package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.project.ArchiveProjectUseCase
import io.github.loskovdm.domain.usecase.project.DeleteProjectUseCase
import io.github.loskovdm.domain.usecase.project.GetActiveProjectsUseCase
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ActiveProjectsListViewModel
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import io.github.loskovdm.timetracker.feature.projects.impl.mapper.ProjectMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class ActiveProjectsListViewModelImpl(
    private val mapper: ProjectMapper,
    getActiveProjectsUseCase: GetActiveProjectsUseCase,
    private val archiveProjectUseCase: ArchiveProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
) : ActiveProjectsListViewModel() {
    override val state: StateFlow<ProjectsListState> = getActiveProjectsUseCase()
        .map { projects ->
            if (projects.isEmpty()) {
                ProjectsListState.Empty
            } else {
                ProjectsListState.Loaded(
                    projects = mapper.toView(projects)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProjectsListState.Loading,
        )

    fun deleteProject(project: Project, deleteStrategy: DeleteStrategy) {
        viewModelScope.launch {
            deleteProjectUseCase(
                id = project.id,
                name = project.name,
                color = project.color,
                isArchived = project.isArchived,
                deleteStrategy = deleteStrategy,
            )
        }
    }

    fun archiveProject(project: Project) {
        viewModelScope.launch {
            archiveProjectUseCase(
                id = project.id,
                name = project.name,
                color = project.color,
            )
        }
    }
}
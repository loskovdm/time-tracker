package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.project.DeleteProjectUseCase
import io.github.loskovdm.domain.usecase.project.GetArchivedProjectsUseCase
import io.github.loskovdm.domain.usecase.project.UnarchiveProjectUseCase
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListViewModel
import io.github.loskovdm.timetracker.feature.projects.impl.mapper.ProjectMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class ArchivedProjectsListViewModel(
    private val mapper: ProjectMapper,
    getArchivedProjectsUseCase: GetArchivedProjectsUseCase,
    private val unarchiveProjectUseCase: UnarchiveProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
) : ProjectsListViewModel() {
    override val state: StateFlow<ProjectsListState> = getArchivedProjectsUseCase()
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

    fun unarchiveProject(project: Project) {
        viewModelScope.launch {
            unarchiveProjectUseCase(
                id = project.id,
                name = project.name,
                color = project.color,
            )
        }
    }
}
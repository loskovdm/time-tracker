package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.EmptyScreen
import io.github.loskovdm.designsystem.component.LoadingScreen
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_project_hint
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.no_projects
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun ActiveProjectsList(
    onEditProjectClick: (Uuid) -> Unit,
    onProjectClick: (projectId: Uuid, projectName: String) -> Unit,
    viewModel: ActiveProjectsListViewModelImpl,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        ProjectsListState.Loading -> LoadingScreen()
        ProjectsListState.Empty -> EmptyScreen(
            icon = vectorResource(Res.drawable.ic_projects_outlined),
            headline = stringResource(Res.string.no_projects),
            hint = stringResource(Res.string.add_project_hint)
        )
        is ProjectsListState.Loaded -> {
            LoadedActiveProjectsList(
                projectsList = currentState.projects,
                onProjectClick = {
                    onProjectClick(it.id, it.name)
                },
                onEditClick = onEditProjectClick,
                onArchivedClick = { project ->
                    viewModel.archiveProject(project)
                },
                onDeleteClick = { project ->
                    viewModel.deleteProject(project)
                },
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun LoadedActiveProjectsList(
    modifier: Modifier = Modifier,
    projectsList: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEditClick: (Uuid) -> Unit,
    onArchivedClick: (Project) -> Unit,
    onDeleteClick: (Project) -> Unit,
) {
    ProjectsList(
        modifier = modifier,
        projectsList = projectsList,
        onProjectClick = onProjectClick,
        onEditClick = onEditClick,
        onArchivedClick = onArchivedClick,
        onUnarchiveClick = null,
        onDeleteClick = onDeleteClick
    )
}
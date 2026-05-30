package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.EmptyScreen
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.designsystem.component.LoadingScreen
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_project_hint
import timetracker.designsystem.generated.resources.empty_archive
import timetracker.designsystem.generated.resources.ic_archive_outlined
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.no_projects
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun ArchivedProjectsList(
    onEditProjectClick: (Uuid) -> Unit,
    onProjectClick: (projectId: Uuid, projectName: String) -> Unit,
    viewModel: ArchivedProjectsListViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        ProjectsListState.Loading -> LoadingScreen()
        ProjectsListState.Empty -> EmptyScreen(
            icon = vectorResource(Res.drawable.ic_archive_outlined),
            headline = stringResource(Res.string.empty_archive),
            hint = "",
        )
        is ProjectsListState.Loaded -> {
            LoadedArchivedProjectsList(
                projectsList = currentState.projects,
                onProjectClick = {
                    onProjectClick(it.id, it.name)
                },
                onEditClick = onEditProjectClick,
                onUnarchivedClick = { project ->
                    viewModel.unarchiveProject(project)
                },
                onDeleteClick = { project, strategy ->
                    viewModel.deleteProject(project, strategy)
                },
            )
        }
    }
}

@Composable
private fun EmptyProjectsList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = vectorResource(Res.drawable.ic_projects_outlined),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.no_projects),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.add_project_hint),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun LoadedArchivedProjectsList(
    modifier: Modifier = Modifier,
    projectsList: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEditClick: (Uuid) -> Unit,
    onUnarchivedClick: (Project) -> Unit,
    onDeleteClick: (Project, DeleteStrategy) -> Unit,
) {
    ProjectsList(
        modifier = modifier,
        projectsList = projectsList,
        onProjectClick = onProjectClick,
        onEditClick = onEditClick,
        onArchivedClick = null,
        onUnarchiveClick = onUnarchivedClick,
        onDeleteClick = onDeleteClick
    )
}
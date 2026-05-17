package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.local.LocalFabPadding
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectEditorDestination
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_project_hint
import timetracker.designsystem.generated.resources.archive
import timetracker.designsystem.generated.resources.delete
import timetracker.designsystem.generated.resources.edit
import timetracker.designsystem.generated.resources.ic_archive_outlined
import timetracker.designsystem.generated.resources.ic_delete_outlined
import timetracker.designsystem.generated.resources.ic_edit_outlined
import timetracker.designsystem.generated.resources.ic_more_vert
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.no_projects
import kotlin.uuid.ExperimentalUuidApi

@Composable
internal fun ProjectsList(
    onEditProjectClick: (ProjectEditorDestination) -> Unit,
    viewModel: ProjectsListViewModelImpl = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        ProjectsListState.Loading -> LoadingProjectsList()
        ProjectsListState.Empty -> EmptyProjectsList()
        is ProjectsListState.Loaded -> {
            LoadedProjectsList(
                projectsList = currentState.projects,
                onProjectClick = {},
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingProjectsList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize().
            background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        LoadingIndicator()
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
private fun LoadedProjectsList(
    modifier: Modifier = Modifier,
    projectsList: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEditClick: (ProjectEditorDestination) -> Unit,
    onArchivedClick: (Project) -> Unit,
    onDeleteClick: (Project) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = LocalFabPadding.current.calculateBottomPadding()
        ),
    ) {
        projectsList.forEach { project ->
            item {
                ProjectItem(
                    name = project.name,
                    color = project.color,
                    onClick = { onProjectClick(project) },
                    onEditClick = {
                        val projectEditorDestination = ProjectEditorDestination(
                            projectsId = project.id
                        )
                        onEditClick(projectEditorDestination)
                    },
                    onArchivedClick = { onArchivedClick(project) },
                    onDeleteClick = { onDeleteClick(project) }
                )
            }
        }
    }
}

@Composable
private fun ProjectItem(
    modifier: Modifier = Modifier,
    name: String,
    color: Long,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onArchivedClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        ProjectItemContent(
            name = name,
            color = color,
            onEditClick = onEditClick,
            onArchivedClick = onArchivedClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
private fun ProjectItemContent(
    modifier: Modifier = Modifier,
    name: String,
    color: Long,
    onEditClick: () -> Unit,
    onArchivedClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color(color))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        MenuButton(
            onEditClick = onEditClick,
            onArchivedClick = onArchivedClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
private fun MenuButton(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onArchivedClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val isVisibleMenu = rememberSaveable { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { isVisibleMenu.value = !isVisibleMenu.value }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_more_vert),
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        DropdownMenu(
            expanded = isVisibleMenu.value,
            onDismissRequest = { isVisibleMenu.value = false },
            shape = RoundedCornerShape(16.dp),
        ) {
            MenuContent(
                onEditClick = onEditClick,
                onArchivedClick = onArchivedClick,
                onDeleteClick = onDeleteClick,
                onClose = { isVisibleMenu.value = false },
            )
        }
    }
}

@Composable
private fun MenuContent(
    onEditClick: () -> Unit,
    onArchivedClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(stringResource(Res.string.edit))
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_edit_outlined),
                contentDescription = null,
            )
        },
        onClick = {
            onEditClick()
            onClose()
        }
    )

    DropdownMenuItem(
        text = {
            Text(stringResource(Res.string.archive))
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_archive_outlined),
                contentDescription = null,
            )
        },
        onClick = {
            onArchivedClick()
            onClose()
        }
    )

    DropdownMenuItem(
        text = {
            Text(stringResource(Res.string.delete))
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_delete_outlined),
                contentDescription = null,
            )
        },
        onClick = {
            onDeleteClick()
            onClose()
        }
    )
}
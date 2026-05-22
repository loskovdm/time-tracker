package io.github.loskovdm.timetracker.feature.projects.impl.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.archive
import timetracker.designsystem.generated.resources.delete
import timetracker.designsystem.generated.resources.edit
import timetracker.designsystem.generated.resources.ic_archive_outlined
import timetracker.designsystem.generated.resources.ic_delete_outlined
import timetracker.designsystem.generated.resources.ic_edit_outlined
import timetracker.designsystem.generated.resources.ic_more_vert
import timetracker.designsystem.generated.resources.ic_unarchive_outlined
import timetracker.designsystem.generated.resources.more_options
import timetracker.designsystem.generated.resources.unarchive
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProjectsList(
    modifier: Modifier = Modifier,
    projectsList: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEditClick: (Uuid) -> Unit,
    onArchivedClick: ((Project) -> Unit)?,
    onUnarchiveClick: ((Project) -> Unit)?,
    onDeleteClick: (Project) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = if (LocalDeviceConfiguration.current != DeviceConfiguration.DESKTOP) {
                80.dp
            } else {
                0.dp
            }
        ),
    ) {
        projectsList.forEach { project ->
            item {
                ProjectItem(
                    name = project.name,
                    color = project.color,
                    onClick = { onProjectClick(project) },
                    onEditClick = {
                        onEditClick(project.id)
                    },
                    onArchivedClick = onArchivedClick?.let { { it(project) } },
                    onUnarchiveClick = onUnarchiveClick?.let { { it(project) } },
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
    onArchivedClick: (() -> Unit)?,
    onUnarchiveClick: (() -> Unit)?,
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
            onUnarchiveClick = onUnarchiveClick,
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
    onArchivedClick: (() -> Unit)?,
    onUnarchiveClick: (() -> Unit)?,
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
            onUnarchiveClick = onUnarchiveClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
private fun MenuButton(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onArchivedClick: (() -> Unit)?,
    onUnarchiveClick: (() -> Unit)?,
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
                contentDescription = stringResource(Res.string.more_options),
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
                onUnarchiveClick = onUnarchiveClick,
                onDeleteClick = onDeleteClick,
                onClose = { isVisibleMenu.value = false },
            )
        }
    }
}

@Composable
private fun MenuContent(
    onEditClick: () -> Unit,
    onArchivedClick: (() -> Unit)?,
    onUnarchiveClick: (() -> Unit)?,
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
            if (onArchivedClick != null) {
                Text(stringResource(Res.string.archive))
            } else {
                Text(stringResource(Res.string.unarchive))
            }
        },
        leadingIcon = {
            if (onArchivedClick != null) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_archive_outlined),
                    contentDescription = null,
                )
            } else {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_unarchive_outlined),
                    contentDescription = null,
                )
            }
        },
        onClick = {
            if (onArchivedClick != null) {
                onArchivedClick()
                onClose()
            } else {
                onUnarchiveClick?.invoke()
                onClose()
            }
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
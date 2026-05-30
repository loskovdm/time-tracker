package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.component.DeleteWithTimeEntriesDialog
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.active
import timetracker.designsystem.generated.resources.completed
import timetracker.designsystem.generated.resources.delete
import timetracker.designsystem.generated.resources.delete_task_message
import timetracker.designsystem.generated.resources.ic_check
import timetracker.designsystem.generated.resources.ic_complete
import timetracker.designsystem.generated.resources.ic_delete_outlined
import timetracker.designsystem.generated.resources.ic_more_vert
import timetracker.designsystem.generated.resources.ic_uncomplete
import timetracker.designsystem.generated.resources.mark_as_active
import timetracker.designsystem.generated.resources.mark_as_done
import timetracker.designsystem.generated.resources.more_options
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun TasksList(
    projectId: Uuid,
    onTaskClick: (projectId: Uuid, taskId: Uuid?) -> Unit,
) {
    val activeTasksListViewModelImpl = koinViewModel<ActiveTasksListViewModelImpl> { parametersOf(projectId) }
    val completedTasksListViewModel = koinViewModel<CompletedTasksListViewModel> { parametersOf(projectId) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val options = listOf(Res.string.active, Res.string.completed)
        var selectedIndex by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        ) {
            options.forEachIndexed { index, stringRes ->
                val checked = selectedIndex == index
                ToggleButton(
                    checked = checked,
                    onCheckedChange = { selectedIndex = index },
                    modifier = Modifier.weight(1f),
                    shapes = when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
                ) {
                    if (checked) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_check),
                            contentDescription = null
                        )
                        Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                    }
                    Text(stringResource(stringRes))
                }
            }
        }
        if (selectedIndex == 0) {
            ActiveTasksList(
                viewModel = activeTasksListViewModelImpl,
                onTaskClick = onTaskClick,
            )
        } else {
            CompletedTasksList(
                viewModel = completedTasksListViewModel,
                onTaskClick = onTaskClick,
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun LoadedTasksList(
    tasksList: List<Task>,
    onTaskClick: (projectId: Uuid, taskId: Uuid) -> Unit,
    onCompleteClick: ((Task) -> Unit)?,
    onActiveClick: ((Task) -> Unit)?,
    onDeleteClick: (Task, DeleteStrategy) -> Unit,
) {
    var pendingDeleteTask by remember { mutableStateOf<Task?>(null) }

    pendingDeleteTask?.let { task ->
        DeleteWithTimeEntriesDialog(
            message = stringResource(Res.string.delete_task_message, task.name),
            onDeleteTimeEntries = {
                onDeleteClick(task, DeleteStrategy.CASCADE)
                pendingDeleteTask = null
            },
            onKeepTimeEntries = {
                onDeleteClick(task, DeleteStrategy.SET_NULL)
                pendingDeleteTask = null
            },
            onDismiss = { pendingDeleteTask = null },
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = if (LocalDeviceConfiguration.current != DeviceConfiguration.DESKTOP) {
                80.dp
            } else {
                0.dp
            }
        ),
    ) {
        tasksList.forEach { task ->
            item {
                TaskItem(
                    task = task,
                    onClick = {
                        onTaskClick(
                            task.projectId,
                            task.id
                        )
                    },
                    onComplete = onCompleteClick?.let { {it(task)} },
                    onActive = onActiveClick?.let { {it(task)} },
                    onDelete = { pendingDeleteTask = task }
                )
                if (tasksList.last() != task) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit,
    onComplete: (() -> Unit)?,
    onActive: (() -> Unit)?,
    onDelete: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = task.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        TaskMenuButton(
            onComplete = onComplete,
            onActive = onActive,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun TaskMenuButton(
    modifier: Modifier = Modifier,
    onComplete: (() -> Unit)?,
    onActive: (() -> Unit)?,
    onDelete: () -> Unit,
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
            TaskMenuContent(
                onComplete = onComplete,
                onActive = onActive,
                onDelete = onDelete,
                onClose = { isVisibleMenu.value = false },
            )
        }
    }
}

@Composable
private fun TaskMenuContent(
    onComplete: (() -> Unit)?,
    onActive: (() -> Unit)?,
    onDelete: () -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = if (onComplete != null) {
                    stringResource(Res.string.mark_as_done)
                } else {
                    stringResource(Res.string.mark_as_active)
                }
            )
        },
        leadingIcon = {
            if (onComplete != null) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_complete),
                    contentDescription = null,
                )
            } else {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_uncomplete),
                    contentDescription = null,
                )
            }
        },
        onClick = {
            if (onComplete != null) {
                onComplete()
                onClose()
            } else {
                onActive?.invoke()
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
            onDelete()
            onClose()
        }
    )
}
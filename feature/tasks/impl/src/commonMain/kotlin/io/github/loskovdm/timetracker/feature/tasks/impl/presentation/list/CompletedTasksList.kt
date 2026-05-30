package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.EmptyScreen
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.designsystem.component.LoadingScreen
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_task_hint
import timetracker.designsystem.generated.resources.ic_task_outlined
import timetracker.designsystem.generated.resources.no_completed_tasks
import timetracker.designsystem.generated.resources.no_tasks
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun CompletedTasksList(
    viewModel: CompletedTasksListViewModel,
    onTaskClick: (projectId: Uuid, taskId: Uuid) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        TasksListState.Loading -> LoadingScreen()
        is TasksListState.Empty -> EmptyScreen(
            icon = vectorResource(Res.drawable.ic_task_outlined),
            headline = stringResource(Res.string.no_completed_tasks),
            hint = "",
        )
        is TasksListState.Loaded -> LoadedCompletedTasksList(
            tasksList = currentState.tasks,
            onTaskClick = { projectId, taskId ->
                onTaskClick(projectId, taskId)
            },
            onActiveClick = viewModel::activateTask,
            onDeleteClick = { task, strategy -> viewModel.deleteTask(task, strategy) },
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun LoadedCompletedTasksList(
    tasksList: List<Task>,
    onTaskClick: (projectId: Uuid, taskId: Uuid) -> Unit,
    onActiveClick: (Task) -> Unit,
    onDeleteClick: (Task, DeleteStrategy) -> Unit,
) {
    LoadedTasksList(
        tasksList = tasksList,
        onTaskClick = onTaskClick,
        onCompleteClick = null,
        onActiveClick = onActiveClick,
        onDeleteClick = onDeleteClick,
    )
}
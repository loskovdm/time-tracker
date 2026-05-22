package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list

import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.task.CompleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.DeleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.GetActiveTasksUseCase
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.ActiveTasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListState
import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.TaskMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class ActiveTasksListViewModelImpl(
    private val mapper: TaskMapper,
    @InjectedParam projectId: Uuid,
    getActiveTasksUseCase: GetActiveTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ActiveTasksListViewModel() {
    override val state: StateFlow<TasksListState> = getActiveTasksUseCase(projectId)
        .map { tasks ->
            if (tasks.isEmpty()) {
                TasksListState.Empty
            } else {
                TasksListState.Loaded(
                    tasks = mapper.toView(tasks)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TasksListState.Loading,
        )

    fun completeTask(task: Task) {
        viewModelScope.launch {
            completeTaskUseCase(
                id = task.id,
                name = task.name,
                projectId = task.projectId,
            )
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(
                id = task.id,
                name = task.name,
                projectId = task.projectId,
                isCompleted = task.isCompleted,
                deleteStrategy = DeleteStrategy.CASCADE,
            )
        }
    }
}
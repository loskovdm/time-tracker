package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.task.ActivateTaskUseCase
import io.github.loskovdm.domain.usecase.task.DeleteTaskUseCase
import io.github.loskovdm.domain.usecase.task.GetCompletedTasksUseCase
import io.github.loskovdm.domain.util.DeleteStrategy
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
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
internal class CompletedTasksListViewModel(
    @InjectedParam projectId: Uuid,
    getCompletedTasksUseCase: GetCompletedTasksUseCase,
    private val mapper: TaskMapper,
    private val activateTaskUseCase: ActivateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {
    val state: StateFlow<TasksListState> = getCompletedTasksUseCase(projectId)
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

    fun activateTask(task: Task) {
        viewModelScope.launch {
            activateTaskUseCase(
                id = task.id,
                name = task.name,
                projectId = task.projectId,
            )
        }
    }

    fun deleteTask(task: Task, deleteStrategy: DeleteStrategy) {
        viewModelScope.launch {
            deleteTaskUseCase(
                id = task.id,
                name = task.name,
                projectId = task.projectId,
                isCompleted = task.isCompleted,
                deleteStrategy = deleteStrategy,
            )
        }
    }
}
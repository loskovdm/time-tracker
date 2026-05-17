package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.list

import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.task.GetTasksUseCase
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListState
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.impl.mapper.TaskMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TasksListViewModelImpl(
    private val mapper: TaskMapper,
    getTasksUseCase: GetTasksUseCase,
//    projectId: Uuid,
) : TasksListViewModel() {
    // TODO: Доделать загрузку задач по определенному проекту
    private val tempUuid = Uuid.generateV7()
    override val state: StateFlow<TasksListState> = getTasksUseCase(tempUuid)
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
}
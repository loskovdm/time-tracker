package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.task.AddTaskUseCase
import io.github.loskovdm.domain.usecase.task.GetTaskByIdUseCase
import io.github.loskovdm.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.koin.core.annotation.InjectedParam
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.invalid_characters_error
import timetracker.designsystem.generated.resources.longer_100_characters_error
import timetracker.designsystem.generated.resources.name_empty_error
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal data class TaskEditorArgs(
    val projectId: Uuid,
    val taskId: Uuid?,
)

@OptIn(ExperimentalUuidApi::class)
internal class TaskEditorViewModel(
    @InjectedParam args: TaskEditorArgs,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {
    private val projectId = args.projectId
    private val taskId = args.taskId
    private val _state = MutableStateFlow(TaskEditorState())
    val state = _state
        .onStart { loadTask()  }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskEditorState()
        )

    private fun loadTask() {
        if (taskId == null) {
            _state.update { TaskEditorState() }
        } else {
            viewModelScope.launch {
                val task = getTaskByIdUseCase(taskId)
                if (task == null) {
                    _state.update { TaskEditorState() }
                } else {
                    _state.update {
                        TaskEditorState(
                            name = task.name,
                            isCompleted = task.isCompleted,
                            isNew = false,
                        )
                    }
                }
            }
        }
    }

    fun saveTask(): Boolean {
        val error = validateName(state.value.name)

        if (error == null) {
            viewModelScope.launch {
                if (taskId == null) {
                    addTaskUseCase(
                        name = state.value.name.trim(),
                        projectId = projectId,
                    )
                } else {
                    updateTaskUseCase(
                        id = taskId,
                        projectId = projectId,
                        name = state.value.name.trim(),
                        isCompleted = state.value.isCompleted,
                    )
                }
            }
            return true
        } else {
            _state.update { it.copy(validationError = error) }
            return false
        }
    }

    fun changeName(changedName: String) {
        _state.update {
            it.copy(
                name = changedName,
                validationError = validateName(changedName),
            )
        }
    }

    private fun validateName(name: String): StringResource? {
        return when {
            name.isEmpty() -> Res.string.name_empty_error
            name.length >= 100 -> Res.string.longer_100_characters_error
            name.any { it in "/\\\"?*%&@<>|"} -> Res.string.invalid_characters_error
            else -> null
        }
    }
}
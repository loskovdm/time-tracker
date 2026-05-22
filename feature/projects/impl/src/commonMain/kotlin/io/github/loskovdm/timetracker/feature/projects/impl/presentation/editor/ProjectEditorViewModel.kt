package io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.project.AddProjectUseCase
import io.github.loskovdm.domain.usecase.project.GetProjectByIdUseCase
import io.github.loskovdm.domain.usecase.project.UpdateProjectUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.invalid_characters_error
import timetracker.designsystem.generated.resources.longer_100_characters_error
import timetracker.designsystem.generated.resources.name_empty_error
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class ProjectEditorViewModel(
    private val projectId: Uuid?,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val addProjectUseCase: AddProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectEditorState())
    val state = _state
        .onStart { loadProject() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProjectEditorState(),
        )

    private fun loadProject() {
        if (projectId == null) {
            _state.update { ProjectEditorState() }
        } else {
            viewModelScope.launch {
                val project = getProjectByIdUseCase(projectId)
                if (project == null) {
                    _state.update { ProjectEditorState() }
                } else {
                    _state.update {
                        ProjectEditorState(
                            name = project.name,
                            color = ProjectColor.fromArgb(project.color) ?: ProjectColor.random(),
                            isArchived = project.isArchived,
                            isNew = false,
                        )
                    }
                }
            }
        }
    }

    fun saveProject(): Boolean {
        val error = validateName(state.value.name)

        if (error == null) {
            viewModelScope.launch {
                if (projectId == null) {
                    addProjectUseCase(
                        name = state.value.name.trim(),
                        color = state.value.color.argb,
                    )
                } else {
                    updateProjectUseCase(
                        id = projectId,
                        name = state.value.name.trim(),
                        color = state.value.color.argb,
                        isArchived = state.value.isArchived,
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

    fun colorChange(changedColor: ProjectColor) {
        _state.update { it.copy(color = changedColor) }
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
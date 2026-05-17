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
import timetracker.designsystem.generated.resources.project_name_empty_error
import timetracker.designsystem.generated.resources.project_name_invalid_characters
import timetracker.designsystem.generated.resources.project_name_longer_100_characters
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
                            isNew = false,
                        )
                    }
                }
            }
        }
    }

    fun onSaveProject(): Boolean {
        val error = validateName(state.value.name)

        if (error == null) {
            viewModelScope.launch {
                if (projectId == null) {
                    addProjectUseCase(
                        name = state.value.name,
                        color = state.value.color.argb,
                    )
                } else {
                    updateProjectUseCase(
                        id = projectId,
                        name = state.value.name,
                        color = state.value.color.argb,
                    )
                }
            }
            return true
        } else {
            _state.update { it.copy(validationError = error) }
            return false
        }
    }

    fun onNameChanged(changedName: String) {
        val newName = changedName.trim()
        _state.update {
            it.copy(
                name = newName,
                validationError = validateName(newName),
            )
        }
    }

    fun onColorChanged(changedColor: ProjectColor) {
        _state.update { it.copy(color = changedColor) }
    }

    private fun validateName(name: String): StringResource? {
        return when {
            name.isEmpty() -> Res.string.project_name_empty_error
            name.length >= 100 -> Res.string.project_name_longer_100_characters
            name.any { it in "/\\\"?*%&@<>|"} -> Res.string.project_name_invalid_characters
            else -> null
        }
    }
}
package io.github.loskovdm.projects.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.project.AddProjectUseCase
import io.github.loskovdm.domain.usecase.project.ObserveProjectsUseCase
import io.github.loskovdm.projects.model.toView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val observeProjects: ObserveProjectsUseCase, // Flow<List<Project>>
    private val addProject: AddProjectUseCase            // suspend (name: String) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeProjects()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Unknown error")
                    }
                }
                .collect { projects ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            projects = projects.map { it.toView() }
                        )
                    }
                }
        }
    }

    fun onProjectNameChange(value: String) {
        _uiState.update { it.copy(projectName = value) }
    }

    fun onAddClick() {
        val name = _uiState.value.projectName.trim()
        if (name.isEmpty()) return

        viewModelScope.launch {
            runCatching { addProject(name, 0x000000) }
                .onSuccess {
                    _uiState.update { it.copy(projectName = "", error = null) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message ?: "Failed to add project") }
                }
        }
    }
}
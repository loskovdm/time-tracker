package io.github.loskovdm.projects.projects

import androidx.compose.foundation.text.input.TextFieldState
import io.github.loskovdm.projects.model.Project

data class ProjectsUiState(
    val projectName: String = "",
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
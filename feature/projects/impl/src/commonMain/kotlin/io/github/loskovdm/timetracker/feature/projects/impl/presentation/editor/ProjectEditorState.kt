package io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor

import org.jetbrains.compose.resources.StringResource

internal data class ProjectEditorState(
    val name: String = "",
    val color: ProjectColor = ProjectColor.random(),
    val isArchived: Boolean = false,
    val isNew: Boolean = true,
    val validationError: StringResource? = null,
)
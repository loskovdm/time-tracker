package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor

import org.jetbrains.compose.resources.StringResource

internal data class TaskEditorState(
    val name: String = "",
    val isCompleted: Boolean = false,
    val isNew: Boolean = true,
    val validationError: StringResource? = null,
)

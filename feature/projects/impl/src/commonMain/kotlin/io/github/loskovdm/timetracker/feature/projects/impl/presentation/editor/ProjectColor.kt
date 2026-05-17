package io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.blue
import timetracker.designsystem.generated.resources.green
import timetracker.designsystem.generated.resources.orange
import timetracker.designsystem.generated.resources.pink
import timetracker.designsystem.generated.resources.purple
import timetracker.designsystem.generated.resources.red
import timetracker.designsystem.generated.resources.teal
import timetracker.designsystem.generated.resources.yellow

internal data class ProjectColor(
    val argb: Long,
    val name: StringResource,
) {
    val color: Color get() = Color(argb)

    companion object {
        val entries = listOf(
            ProjectColor(0xFF7E57C2, Res.string.purple),
            ProjectColor(0xFFEC407A, Res.string.pink),
            ProjectColor(0xFFEF5350, Res.string.red),
            ProjectColor(0xFFFF7043, Res.string.orange),
            ProjectColor(0xFFFFCA28, Res.string.yellow),
            ProjectColor(0xFF9CCC65, Res.string.green),
            ProjectColor(0xFF42A5F5, Res.string.blue),
            ProjectColor(0xFF26A69A, Res.string.teal)
        )

        fun random(): ProjectColor = entries.random()
        fun fromArgb(argb: Long): ProjectColor? = entries.find { it.argb == argb }
    }
}
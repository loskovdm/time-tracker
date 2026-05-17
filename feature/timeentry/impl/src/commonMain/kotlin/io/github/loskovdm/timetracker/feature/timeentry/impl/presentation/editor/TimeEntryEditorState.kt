package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.editor

import io.github.loskovdm.designsystem.util.withoutSeconds
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

internal data class TimeEntryEditorState(
    val startDateTime: Instant =
        Clock.System.now().withoutSeconds(TimeZone.currentSystemDefault()),
    val endDateTime: Instant? =
        Clock.System.now().withoutSeconds(TimeZone.currentSystemDefault()) + 1.hours,
    val project: Project? = null,
    val task: Task? = null,
    val isNewEntry: Boolean = true,
    val isDateTimeValid: Boolean = true,
    val duration: Duration = Duration.ZERO,
)
package io.github.loskovdm.timetracker.feature.timeentry.api.presentation

import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntryWithRelations
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration

sealed interface TimerState {
    data class Loaded(
        val timeEntryWithRelations: TimeEntryWithRelations,
        val duration: Duration,
    ) : TimerState
    data object Empty : TimerState
    data class Error(val message: StringResource) : TimerState
}
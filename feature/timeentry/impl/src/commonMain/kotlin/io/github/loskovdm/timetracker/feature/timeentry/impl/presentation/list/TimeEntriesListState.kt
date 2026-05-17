package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.list

import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntryWithRelations

internal sealed interface TimeEntriesListState {
    data object Loading : TimeEntriesListState
    data class Loaded(val completedTimeEntries: List<TimeEntryWithRelations>) : TimeEntriesListState
    data object Empty : TimeEntriesListState
}
package io.github.loskovdm.timetracker.feature.timeentry.api.destination

import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntryEditorDestination (val timeEntryId: Uuid? = null) : TimeTrackerDestination
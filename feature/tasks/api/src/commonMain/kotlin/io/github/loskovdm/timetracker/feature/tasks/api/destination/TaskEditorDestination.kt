package io.github.loskovdm.timetracker.feature.tasks.api.destination

import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TaskEditorDestination(
    val projectId: Uuid,
    val taskId: Uuid? = null,
) : TimeTrackerDestination
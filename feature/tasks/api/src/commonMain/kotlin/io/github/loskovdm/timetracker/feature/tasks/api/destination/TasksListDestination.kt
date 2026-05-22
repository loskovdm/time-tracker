package io.github.loskovdm.timetracker.feature.tasks.api.destination

import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TasksListDestination(val projectId: Uuid, val projectName: String) : TimeTrackerDestination
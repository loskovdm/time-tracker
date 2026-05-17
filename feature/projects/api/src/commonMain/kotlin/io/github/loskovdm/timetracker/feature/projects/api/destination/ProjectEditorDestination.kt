package io.github.loskovdm.timetracker.feature.projects.api.destination

import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ProjectEditorDestination (val projectsId: Uuid? = null) : TimeTrackerDestination
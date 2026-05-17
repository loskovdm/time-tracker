package io.github.loskovdm.timetracker.feature.tasks.api.model

import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Task (
    val id: Uuid,
    val name: String,
    val projectId: Uuid,
)
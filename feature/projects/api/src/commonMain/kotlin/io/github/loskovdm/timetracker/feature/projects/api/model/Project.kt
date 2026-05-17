package io.github.loskovdm.timetracker.feature.projects.api.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project (
    val id: Uuid,
    val name: String,
    val color: Long,
)
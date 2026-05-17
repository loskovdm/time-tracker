package io.github.loskovdm.timetracker.repository.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Task @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val projectId: Uuid,
)
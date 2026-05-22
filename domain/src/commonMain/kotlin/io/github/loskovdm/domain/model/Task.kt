package io.github.loskovdm.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Task (
    val id: Uuid,
    val name: String,
    val projectId: Uuid,
    val isCompleted: Boolean,
)

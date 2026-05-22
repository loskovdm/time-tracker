package io.github.loskovdm.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project (
    val id: Uuid,
    val name: String,
    val color: Long,
    val isArchived: Boolean
)

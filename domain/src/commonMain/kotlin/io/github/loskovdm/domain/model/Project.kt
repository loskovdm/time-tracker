package io.github.loskovdm.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Project @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val color: Long,
    val isSynced: Boolean,
)

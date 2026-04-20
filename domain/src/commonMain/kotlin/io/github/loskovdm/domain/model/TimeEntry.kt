package io.github.loskovdm.domain.model

import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntry(
    val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val projectId: Uuid?,
    val taskId: Uuid?,
    val isSynced: Boolean,
)
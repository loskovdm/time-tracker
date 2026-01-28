package io.github.loskovdm.domain.model

import kotlinx.datetime.LocalDateTime

data class TimeEntry(
    val id: Long = 0,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val projectId: Long?,
    val taskId: Long?,
)
package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StopTrackTimeUseCase(
    private val repository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        id: Uuid,
        startDateTime: Instant,
        endDateTime: Instant,
        projectId: Uuid?,
        taskId: Uuid?,
    ) {
        val timeEntry = TimeEntry(
            id = id,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            projectId = projectId,
            taskId = taskId,
            isSynced = false,
        )
        repository.updateTimeEntry(
            timeEntry = timeEntry,
            updatedAt = Clock.System.now(),
        )
    }
}
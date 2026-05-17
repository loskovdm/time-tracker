package io.github.loskovdm.domain.usecase.timeentry

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddTimeEntryUseCase(
    private val repository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        startDateTime: Instant,
        endDateTime: Instant,
        projectId: Uuid?,
        taskId: Uuid?,
    ) {
        val timeEntry = TimeEntry(
            id = Uuid.generateV7(),
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            projectId = projectId,
            taskId = taskId,
        )
        repository.addTimeEntry(timeEntry = timeEntry)
    }
}
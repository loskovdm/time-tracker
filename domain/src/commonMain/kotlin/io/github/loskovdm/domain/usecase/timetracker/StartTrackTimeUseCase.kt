package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.error.DomainError
import io.github.loskovdm.domain.error.Result
import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StartTrackTimeUseCase(
    private val repository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        startDateTime: Instant,
        projectId: Uuid?,
        taskId: Uuid?,
    ): Result<Unit> {
        val hasActiveTimeEntry = repository.observeTimeEntries()
            .first()
            .any { entry -> entry.endDateTime == null }

        if (hasActiveTimeEntry) {
            return Result.Failure(DomainError.SecondActiveTimeEntry)
        }

        val timeEntry = TimeEntry(
            id = Uuid.generateV7(),
            startDateTime = startDateTime,
            endDateTime = null,
            projectId = projectId,
            taskId = taskId,
            isSynced = false,
        )
        repository.addTimeEntry(
            timeEntry = timeEntry,
            addedAt = Clock.System.now(),
        )

        return Result.Success(Unit)
    }
}
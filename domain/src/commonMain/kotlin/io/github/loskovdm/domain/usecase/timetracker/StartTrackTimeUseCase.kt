package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.error.DomainError
import io.github.loskovdm.domain.error.Result
import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StartTrackTimeUseCase(
    private val timeEntryWithRelationsRepository: TimeEntryWithRelationsRepository,
    private val timeEntryRepository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        startDateTime: Instant,
        projectId: Uuid?,
        taskId: Uuid?,
    ): Result<Unit> {
        val hasActiveTimeEntry = timeEntryWithRelationsRepository.getTimeEntriesWithRelations()
            .first()
            .any { timeEntryWithRelations -> timeEntryWithRelations.timeEntry.endDateTime == null }

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
        timeEntryRepository.addTimeEntry(
            timeEntry = timeEntry,
            addedAt = Clock.System.now(),
        )

        return Result.Success(Unit)
    }
}
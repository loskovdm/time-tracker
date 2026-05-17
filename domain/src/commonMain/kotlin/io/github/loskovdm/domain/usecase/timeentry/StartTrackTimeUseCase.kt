package io.github.loskovdm.domain.usecase.timeentry

import io.github.loskovdm.domain.error.Result
import io.github.loskovdm.domain.error.TrackTimeError
import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StartTrackTimeUseCase(
    private val timeEntryWithRelationsRepository: TimeEntryWithRelationsRepository,
    private val timeEntryRepository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        projectId: Uuid?,
        taskId: Uuid?,
    ): Result<Unit, TrackTimeError> {
        val hasActiveTimeEntry = timeEntryWithRelationsRepository.getTimeEntriesWithRelations()
            .first()
            .any { timeEntryWithRelations -> timeEntryWithRelations.timeEntry.endDateTime == null }

        if (hasActiveTimeEntry) {
            return Result.Failure(TrackTimeError.SecondActiveTimeEntry)
        }

        val now = Clock.System.now()

        val timeEntry = TimeEntry(
            id = Uuid.generateV7(),
            startDateTime = now,
            endDateTime = null,
            projectId = projectId,
            taskId = taskId,
        )
        timeEntryRepository.addTimeEntry(timeEntry = timeEntry)

        return Result.Success(Unit)
    }
}
package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.datetime.LocalDateTime

class AddTimeEntryUseCase(
    private val repository: TimeEntryRepository,
) {
    suspend operator fun invoke(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        projectId: Long?,
        taskId: Long?,
    ) {
        val timeEntry = TimeEntry(
            startTime = startTime,
            endTime = endTime,
            projectId = projectId,
            taskId = taskId,
        )
        repository.addTimeEntry(timeEntry)
    }
}
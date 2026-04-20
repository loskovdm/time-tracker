package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveActiveTimeEntryUseCase(
    private val timeEntryRepository: TimeEntryRepository,
) {
    operator fun invoke(): Flow<TimeEntry?> {
        return timeEntryRepository.observeTimeEntries().map {
            it.find { entry ->
                entry.endDateTime == null
            }
        }
    }
}
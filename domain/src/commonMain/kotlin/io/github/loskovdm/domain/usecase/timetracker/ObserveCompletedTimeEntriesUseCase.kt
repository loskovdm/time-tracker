package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCompletedTimeEntriesUseCase(
    private val repository: TimeEntryRepository,
) {
    operator fun invoke(): Flow<List<TimeEntry>> {
        return repository.observeTimeEntries().map {
            it.filter { entry ->
                entry.endDateTime != null
            }
        }
    }
}
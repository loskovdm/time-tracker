package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.Flow

class GetAllTimeEntriesUseCase(
    private val repository: TimeEntryRepository,
) {
    suspend operator fun invoke(): Flow<List<TimeEntry>> {
        return repository.getAllTimeEntries()
    }
}
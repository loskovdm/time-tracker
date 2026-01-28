package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.Flow

class GetTimeEntryByIdUseCase(
    private val repository: TimeEntryRepository,
) {
    suspend operator fun invoke(id: Long): Flow<TimeEntry>? {
        require(id > 0) { "ID must be greater then zero" }
        return repository.getTimeEntryById(id)
    }
}
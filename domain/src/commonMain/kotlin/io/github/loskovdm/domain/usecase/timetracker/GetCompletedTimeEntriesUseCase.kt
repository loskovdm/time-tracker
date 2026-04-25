package io.github.loskovdm.domain.usecase.timetracker

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCompletedTimeEntriesUseCase(
    private val repository: TimeEntryWithRelationsRepository,
) {
    operator fun invoke(): Flow<List<TimeEntryWithRelations>> {
        return repository.getTimeEntriesWithRelations().map {
            it.filter { timeEntryWithProjectAndTask ->
                timeEntryWithProjectAndTask.timeEntry.endDateTime != null
            }
        }
    }
}
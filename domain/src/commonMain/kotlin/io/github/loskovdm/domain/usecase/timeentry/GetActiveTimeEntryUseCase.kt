package io.github.loskovdm.domain.usecase.timeentry

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetActiveTimeEntryUseCase(
    private val repository: TimeEntryWithRelationsRepository,
) {
    operator fun invoke(): Flow<TimeEntryWithRelations?> {
        return repository.getTimeEntriesWithRelations().map {
            it.find { timeEntryWithRelation ->
                timeEntryWithRelation.timeEntry.endDateTime == null
            }
        }
    }
}
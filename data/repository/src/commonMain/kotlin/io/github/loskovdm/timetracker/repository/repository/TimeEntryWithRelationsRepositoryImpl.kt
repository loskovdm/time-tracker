package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryWithRelationsDataSource
import io.github.loskovdm.timetracker.repository.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimeEntryWithRelationsRepositoryImpl(
    private val localDataSource: TimeEntryWithRelationsDataSource,
): TimeEntryWithRelationsRepository {
    override fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>> {
        return localDataSource.getTimeEntriesWithRelations().map { domainTimeEntries ->
            domainTimeEntries.map { domainTimeEntry -> domainTimeEntry.toDomain() }
        }
    }
}
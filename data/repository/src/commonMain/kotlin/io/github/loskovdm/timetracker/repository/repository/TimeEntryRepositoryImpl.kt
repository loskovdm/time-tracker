package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.model.toDomain
import io.github.loskovdm.timetracker.repository.model.toRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TimeEntryRepositoryImpl (
    private val localDataSource: TimeEntryLocalDataSource,
): TimeEntryRepository {
    override suspend fun addTimeEntry(
        timeEntry: TimeEntry,
        addedAt: Instant
    ) {
        localDataSource.addTimeEntry(timeEntry.toRepo(addedAt))
    }

    override suspend fun updateTimeEntry(
        timeEntry: TimeEntry,
        updatedAt: Instant
    ) {
        localDataSource.updateTimeEntry(timeEntry.toRepo(updatedAt))
    }

    override suspend fun deleteTimeEntry(
        timeEntry: TimeEntry,
        deletedAt: Instant
    ) {
        localDataSource.deleteTimeEntry(timeEntry.toRepo(deletedAt))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTimeEntryById(id: Uuid): TimeEntry? {
        return localDataSource.getTimeEntryById(id)?.toDomain()
    }

    override fun observeTimeEntries(): Flow<List<TimeEntry>> {
        return localDataSource.observeTimeEntries().map { repoTimeEntries ->
            repoTimeEntries.map { repoTimeEntry -> repoTimeEntry.toDomain() }
        }
    }
}
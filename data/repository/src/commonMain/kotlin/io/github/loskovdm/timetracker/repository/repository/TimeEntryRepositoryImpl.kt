package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.mapper.TimeEntryMapper
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryRepositoryImpl (
    private val localDataSource: TimeEntryLocalDataSource,
    private val mapper: TimeEntryMapper,
): TimeEntryRepository {
    override suspend fun addTimeEntry(timeEntry: TimeEntry) {
        localDataSource.addTimeEntry(mapper.toRepo(timeEntry))
    }

    override suspend fun updateTimeEntry(timeEntry: TimeEntry) {
        localDataSource.updateTimeEntry(mapper.toRepo(timeEntry))
    }

    override suspend fun deleteTimeEntry(timeEntry: TimeEntry) {
        localDataSource.deleteTimeEntry(mapper.toRepo(timeEntry))
    }

    override suspend fun deleteTimeEntryByTaskId(taskId: Uuid) {
        localDataSource.deleteTimeEntryByTaskId(taskId)
    }

    override suspend fun setTaskIdToNull(taskId: Uuid) {
        localDataSource.setTaskIdToNull(taskId)
    }
}
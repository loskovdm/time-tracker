package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.mapper.TimeEntryMapper

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
}
package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.mapper.TimeEntryMapper
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlin.uuid.Uuid

internal class TimeEntryLocalDataSourceImpl(
    private val dao: TimeEntryDao,
    private val mapper: TimeEntryMapper,
): TimeEntryLocalDataSource {
    override suspend fun addTimeEntry(timeEntry: TimeEntry) {
        dao.insertTimeEntry(mapper.toEntity(timeEntry))
    }

    override suspend fun updateTimeEntry(timeEntry: TimeEntry) {
        dao.updateTimeEntry(mapper.toEntity(timeEntry))
    }

    override suspend fun deleteTimeEntry(timeEntry: TimeEntry) {
        dao.deleteTimeEntry(mapper.toEntity(timeEntry))
    }

    override suspend fun deleteTimeEntryByTaskId(taskId: Uuid) {
        dao.deleteTimeEntryByTaskId(taskId)
    }

    override suspend fun setTaskIdToNull(taskId: Uuid) {
        dao.setTaskIdToNull(taskId)
    }
}
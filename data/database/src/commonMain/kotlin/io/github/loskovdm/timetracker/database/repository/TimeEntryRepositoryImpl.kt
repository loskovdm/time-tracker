package io.github.loskovdm.timetracker.database.repository

import io.github.loskovdm.domain.model.TimeEntry
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.mapper.TimeEntryMapper
import kotlin.uuid.Uuid

internal class TimeEntryRepositoryImpl(
    private val dao: TimeEntryDao,
    private val mapper: TimeEntryMapper,
) : TimeEntryRepository{
    override suspend fun addTimeEntry(timeEntry: TimeEntry) {
        dao.insertTimeEntry(mapper.toEntityForInsert(timeEntry))
    }

    override suspend fun updateTimeEntry(timeEntry: TimeEntry) {
        val existing = dao.getTimeEntryById(timeEntry.id) ?: return
        dao.updateTimeEntry(mapper.toEntityForUpdate(timeEntry, existing))
    }

    override suspend fun deleteTimeEntry(timeEntry: TimeEntry) {
        val existing = dao.getTimeEntryById(timeEntry.id) ?: return
        dao.deleteTimeEntry(mapper.toEntityForUpdate(timeEntry, existing))
    }

    override suspend fun deleteTimeEntryByTaskId(taskId: Uuid) {
        dao.deleteTimeEntryByTaskId(taskId)
    }

    override suspend fun setTaskIdToNull(taskId: Uuid) {
        dao.setTaskIdToNull(taskId)
    }

    override suspend fun deleteTimeEntryByProjectId(projectId: Uuid) {
        dao.deleteTimeEntryByProjectId(projectId)
    }

    override suspend fun unlinkTimeEntriesFromProject(projectId: Uuid) {
        dao.unlinkTimeEntriesFromProject(projectId)
    }
}
package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.model.toEntity
import io.github.loskovdm.timetracker.database.model.toRepo
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlin.uuid.Uuid

class TimeEntryLocalDataSourceImpl(private val dao: TimeEntryDao): TimeEntryLocalDataSource {
    override suspend fun addTimeEntry(timeEntry: TimeEntry) {
        dao.insertTimeEntry(timeEntry.toEntity())
    }

    override suspend fun updateTimeEntry(timeEntry: TimeEntry) {
        dao.updateTimeEntry(timeEntry.toEntity())
    }

    override suspend fun getTimeEntryById(id: Uuid): TimeEntry? {
        return dao.getTimeEntryById(id)?.toRepo()
    }
}
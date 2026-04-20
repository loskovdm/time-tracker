package io.github.loskovdm.timetracker.database.dataSource

import io.github.loskovdm.timetracker.database.dao.TimeEntryDao
import io.github.loskovdm.timetracker.database.entity.toEntity
import io.github.loskovdm.timetracker.database.entity.toRepo
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryLocalDataSource
import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class TimeEntryLocalDataSourceImpl(private val dao: TimeEntryDao): TimeEntryLocalDataSource {
    override suspend fun addTimeEntry(timeEntry: TimeEntry) {
        dao.insertTimeEntry(timeEntry.toEntity())
    }

    override suspend fun updateTimeEntry(timeEntry: TimeEntry) {
        dao.updateTimeEntry(timeEntry.toEntity())
    }

    override suspend fun deleteTimeEntry(timeEntry: TimeEntry) {
        dao.deleteTimeEntry(timeEntry.toEntity())
    }

    override suspend fun getTimeEntryById(id: Uuid): TimeEntry? {
        return dao.getTimeEntryById(id)?.toRepo()
    }

    override fun observeTimeEntries(): Flow<List<TimeEntry>> {
        return dao.observeTimeEntries().map { timeEntryEntities ->
            timeEntryEntities.map { timeEntryEntity -> timeEntryEntity.toRepo() }
        }
    }
}
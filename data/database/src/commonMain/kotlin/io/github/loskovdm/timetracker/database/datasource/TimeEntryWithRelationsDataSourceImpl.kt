package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.model.toRepo
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryWithRelationsDataSource
import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class TimeEntryWithRelationsDataSourceImpl(
    private val dao: TimeEntryWithRelationsDao
): TimeEntryWithRelationsDataSource {
    override fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>> {
        return dao.getTimeEntriesWithRelations().map { entityTimeEntries ->
            entityTimeEntries.map { entityTimeEntry -> entityTimeEntry.toRepo() }
        }
    }
}
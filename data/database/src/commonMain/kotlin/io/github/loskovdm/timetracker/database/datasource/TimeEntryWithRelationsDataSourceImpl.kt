package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.mapper.TimeEntryWithRelationsMapper
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryWithRelationsDataSource
import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class TimeEntryWithRelationsDataSourceImpl(
    private val dao: TimeEntryWithRelationsDao,
    private val mapper: TimeEntryWithRelationsMapper,
): TimeEntryWithRelationsDataSource {
    override fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>> {
        return dao.getTimeEntriesWithRelations().map { mapper.toRepo(it) }
    }

    override suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations? {
        return dao.getTimeEntryWithRelationsById(id)?.let { mapper.toRepo(it) }
    }
}
package io.github.loskovdm.timetracker.database.repository

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import io.github.loskovdm.timetracker.database.dao.TimeEntryWithRelationsDao
import io.github.loskovdm.timetracker.database.mapper.TimeEntryWithRelationsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class TimeEntryWithRelationsRepositoryImpl(
    private val dao: TimeEntryWithRelationsDao,
    private val mapper: TimeEntryWithRelationsMapper,
) : TimeEntryWithRelationsRepository {
    override fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>> {
        return dao.getTimeEntriesWithRelations().map { mapper.toDomain(it) }
    }

    override fun watchActiveTimeEntry(): Flow<TimeEntryWithRelations?> {
        return dao.watchActiveTimeEntry().map { entry ->
            entry?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations? {
        return dao.getTimeEntryWithRelationsById(id)?.let { mapper.toDomain(it) }
    }
}
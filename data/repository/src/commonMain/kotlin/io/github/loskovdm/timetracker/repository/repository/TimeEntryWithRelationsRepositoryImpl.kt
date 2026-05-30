package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import io.github.loskovdm.timetracker.repository.datasource.TimeEntryWithRelationsDataSource
import io.github.loskovdm.timetracker.repository.mapper.TimeEntryWithRelationsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryWithRelationsRepositoryImpl(
    private val localDataSource: TimeEntryWithRelationsDataSource,
    private val mapper: TimeEntryWithRelationsMapper,
): TimeEntryWithRelationsRepository {
    override fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>> {
        return localDataSource.getTimeEntriesWithRelations().map {
            mapper.toDomain(it)
        }
    }

    override fun watchActiveTimeEntry(): Flow<TimeEntryWithRelations?> {
        return localDataSource.watchActiveTimeEntry().map { entry ->
            entry?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations? {
        return localDataSource.getTimeEntryWithRelationsById(id)?.let {
            mapper.toDomain(it)
        }
    }
}
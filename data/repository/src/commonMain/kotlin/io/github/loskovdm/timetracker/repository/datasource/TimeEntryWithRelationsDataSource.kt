package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TimeEntryWithRelationsDataSource {
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>
    suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations?
}
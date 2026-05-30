package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TimeEntryWithRelationsRepository {
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>
    fun watchActiveTimeEntry(): Flow<TimeEntryWithRelations?>
    suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations?
}
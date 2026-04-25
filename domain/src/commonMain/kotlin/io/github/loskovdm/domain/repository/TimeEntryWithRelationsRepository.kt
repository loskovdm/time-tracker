package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow

interface TimeEntryWithRelationsRepository {
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>
}
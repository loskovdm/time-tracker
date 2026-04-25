package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow

interface TimeEntryWithRelationsDataSource {
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>
}
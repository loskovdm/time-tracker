package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntry
import kotlinx.coroutines.flow.Flow

interface TimeEntryRepository {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
    suspend fun getTimeEntryById(id: Long): Flow<TimeEntry>?
    suspend fun getAllTimeEntries(): Flow<List<TimeEntry>>
}
package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntry
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TimeEntryRepository {
    suspend fun addTimeEntry(
        timeEntry: TimeEntry,
        addedAt: Instant,
    )
    suspend fun updateTimeEntry(
        timeEntry: TimeEntry,
        updatedAt: Instant,
    )
    suspend fun deleteTimeEntry(
        timeEntry: TimeEntry,
        deletedAt: Instant,
    )
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getTimeEntryById(id: Uuid): TimeEntry?
    fun observeTimeEntries(): Flow<List<TimeEntry>>
}
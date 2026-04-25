package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TimeEntryLocalDataSource {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getTimeEntryById(id: Uuid): TimeEntry?
}
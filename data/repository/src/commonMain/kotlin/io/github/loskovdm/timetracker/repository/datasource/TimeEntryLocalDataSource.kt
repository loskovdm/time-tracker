package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.TimeEntry

interface TimeEntryLocalDataSource {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
}
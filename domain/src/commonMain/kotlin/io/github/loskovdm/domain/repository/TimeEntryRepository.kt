package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntry

interface TimeEntryRepository {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
}
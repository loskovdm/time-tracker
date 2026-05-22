package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TimeEntryLocalDataSource {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntryByTaskId(taskId: Uuid)
    suspend fun setTaskIdToNull(taskId: Uuid)
}
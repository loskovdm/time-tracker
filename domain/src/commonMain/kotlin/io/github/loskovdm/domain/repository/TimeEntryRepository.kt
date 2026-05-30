package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.TimeEntry
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TimeEntryRepository {
    suspend fun addTimeEntry(timeEntry: TimeEntry)
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
    suspend fun deleteTimeEntryByTaskId(taskId: Uuid)
    suspend fun setTaskIdToNull(taskId: Uuid)
    suspend fun deleteTimeEntryByProjectId(projectId: Uuid)
    suspend fun unlinkTimeEntriesFromProject(projectId: Uuid)
}
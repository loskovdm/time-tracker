package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao

import androidx.room.Delete

import androidx.room.Insert

import androidx.room.Query

import androidx.room.Update

import io.github.loskovdm.timetracker.database.model.TimeEntry

import kotlin.uuid.Uuid

@Dao
interface TimeEntryDao {
    @Insert
    suspend fun insertTimeEntry(timeEntry: TimeEntry)

    @Delete
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)

    @Update
    suspend fun updateTimeEntry(timeEntry: TimeEntry)

    @Query("SELECT * FROM time_entries WHERE id = :id LIMIT 1")
    suspend fun getTimeEntryById(id: Uuid): TimeEntry?

    @Query("DELETE FROM time_entries WHERE task_id = :taskId")
    suspend fun deleteTimeEntryByTaskId(taskId: Uuid)

    @Query("UPDATE time_entries SET task_id = NULL WHERE task_id = :taskId")
    suspend fun setTaskIdToNull(taskId: Uuid)

    @Query("DELETE FROM time_entries WHERE project_id = :projectId")
    suspend fun deleteTimeEntryByProjectId(projectId: Uuid)

    @Query("UPDATE time_entries SET project_id = NULL, task_id = NULL WHERE project_id = :projectId")
    suspend fun unlinkTimeEntriesFromProject(projectId: Uuid)
}
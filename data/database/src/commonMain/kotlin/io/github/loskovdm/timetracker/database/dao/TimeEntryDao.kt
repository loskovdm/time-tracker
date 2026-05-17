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

    @Query("SELECT * FROM TimeEntry WHERE id = :id LIMIT 1")
    suspend fun getTimeEntryById(id: Uuid): TimeEntry?
}
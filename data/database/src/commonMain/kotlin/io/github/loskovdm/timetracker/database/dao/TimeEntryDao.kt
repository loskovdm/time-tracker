package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.loskovdm.timetracker.database.model.TimeEntry
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TimeEntryDao {
    @Insert
    suspend fun insertTimeEntry(timeEntry: TimeEntry)

    @Update
    suspend fun updateTimeEntry(timeEntry: TimeEntry)

    @Query("SELECT * FROM TimeEntry WHERE id = :id AND isDelete = 0 LIMIT 1")
    suspend fun getTimeEntryById(id: Uuid): TimeEntry?

    @Query("SELECT * FROM TimeEntry WHERE isDelete = 0")
    fun observeTimeEntries(): Flow<List<TimeEntry>>
}
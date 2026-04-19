package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.loskovdm.timetracker.database.entity.TimeEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TimeEntryDao {
    @Insert
    suspend fun insertTimeEntry(timeEntry: TimeEntryEntity)

    @Update
    suspend fun updateTimeEntry(timeEntry: TimeEntryEntity)

    @Delete
    suspend fun deleteTimeEntry(timeEntry: TimeEntryEntity)

    @Query("SELECT * FROM TimeEntryEntity WHERE id = :id LIMIT 1")
    suspend fun getTimeEntryById(id: Uuid): TimeEntryEntity?

    @Query("SELECT * FROM TimeEntryEntity")
    fun observeTimeEntries(): Flow<List<TimeEntryEntity>>
}
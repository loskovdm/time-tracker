package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao

import androidx.room.Query

import androidx.room.Transaction

import io.github.loskovdm.timetracker.database.model.TimeEntryWithRelations

import kotlinx.coroutines.flow.Flow

import kotlin.uuid.Uuid

@Dao
interface TimeEntryWithRelationsDao {
    @Transaction
    @Query("SELECT * FROM time_entries ORDER BY start_date_time DESC")
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>

    @Transaction
    @Query(
        """
        SELECT * FROM time_entries
        WHERE end_date_time IS NULL
        ORDER BY start_date_time DESC
        LIMIT 1
        """,
    )

    fun watchActiveTimeEntry(): Flow<TimeEntryWithRelations?>

    @Transaction
    @Query("SELECT * FROM time_entries WHERE id = :id LIMIT 1")
    suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations?
}
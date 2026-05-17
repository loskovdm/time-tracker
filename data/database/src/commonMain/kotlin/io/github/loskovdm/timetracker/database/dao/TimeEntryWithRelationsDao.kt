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
    @Query("SELECT * FROM TimeEntry ORDER BY startDateTime DESC")
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>

    @Transaction
    @Query("SELECT * FROM TimeEntry WHERE id = :id LIMIT 1")
    suspend fun getTimeEntryWithRelationsById(id: Uuid): TimeEntryWithRelations?
}
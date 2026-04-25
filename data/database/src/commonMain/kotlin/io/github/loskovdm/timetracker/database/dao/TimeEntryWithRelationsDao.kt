package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.github.loskovdm.timetracker.database.model.TimeEntryWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeEntryWithRelationsDao {
    @Transaction
    @Query("SELECT * FROM TimeEntry WHERE isDelete = 0")
    fun getTimeEntriesWithRelations(): Flow<List<TimeEntryWithRelations>>
}
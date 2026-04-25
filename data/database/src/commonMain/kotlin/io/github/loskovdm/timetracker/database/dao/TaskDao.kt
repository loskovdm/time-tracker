package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.loskovdm.timetracker.database.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM Task WHERE id = :id AND isDelete = 0 LIMIT 1")
    suspend fun getTaskById(id: Uuid): Task?

    @Query("SELECT * FROM Task WHERE isDelete = 0")
    fun getTasks(): Flow<List<Task>>
}
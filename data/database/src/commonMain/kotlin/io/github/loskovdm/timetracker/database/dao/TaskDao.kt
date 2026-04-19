package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.loskovdm.timetracker.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM taskentity WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Uuid): TaskEntity?

    @Query("SELECT * FROM TaskEntity")
    fun observeTasks(): Flow<List<TaskEntity>>
}
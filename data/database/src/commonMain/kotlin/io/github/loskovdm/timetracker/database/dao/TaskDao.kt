package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao

import androidx.room.Delete

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

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Uuid): Task?

    @Query("SELECT * FROM tasks WHERE project_id = :projectId AND is_completed = 0")
    fun getActiveTasks(projectId: Uuid): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE project_id = :projectId AND is_completed = 1")
    fun getCompletedTasks(projectId: Uuid): Flow<List<Task>>

    @Query("DELETE FROM tasks WHERE project_id = :projectId")
    suspend fun deleteTasksByProjectId(projectId: Uuid)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :guestUserId")
    suspend fun countGuestTasks(guestUserId: String): Int

    @Query("SELECT * FROM tasks WHERE user_id = :userId")
    suspend fun getTasksByUserId(userId: String): List<Task>
}
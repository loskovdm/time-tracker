package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao

import androidx.room.Delete

import androidx.room.Insert

import androidx.room.Query

import androidx.room.Update

import io.github.loskovdm.timetracker.database.model.Project

import kotlinx.coroutines.flow.Flow

import kotlin.uuid.Uuid

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Uuid): Project?

    @Query("SELECT * FROM projects WHERE is_archived = :isArchived")
    fun getProjects(isArchived: Boolean): Flow<List<Project>>

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects()

    @Query("SELECT COUNT(*) FROM projects WHERE user_id = :guestUserId")
    suspend fun countGuestProjects(guestUserId: String): Int

    @Query("SELECT * FROM projects WHERE user_id = :userId")
    suspend fun getProjectsByUserId(userId: String): List<Project>
}
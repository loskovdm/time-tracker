package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
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

    @Query("SELECT * FROM Project WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Uuid): Project?

    @Query("SELECT * FROM Project WHERE isArchived = :isArchived")
    fun getProjects(isArchived: Boolean): Flow<List<Project>>
}
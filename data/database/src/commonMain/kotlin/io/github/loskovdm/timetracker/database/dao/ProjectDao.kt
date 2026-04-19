package io.github.loskovdm.timetracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.loskovdm.timetracker.database.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("SELECT * FROM ProjectEntity WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Uuid): ProjectEntity?

    @Query("SELECT * FROM ProjectEntity")
    fun observeProjects(): Flow<List<ProjectEntity>>
}
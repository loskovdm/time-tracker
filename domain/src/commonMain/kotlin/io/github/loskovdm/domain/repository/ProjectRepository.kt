package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(project: Project)
    suspend fun getProjectById(id: Long): Flow<Project>?
    suspend fun getAllProjects(): Flow<List<Project>>
}
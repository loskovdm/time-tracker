package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Project
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectRepository {
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun archiveProject(project: Project)
    suspend fun unarchiveProject(project: Project)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getProjectById(id: Uuid): Project?
    fun getProjects(isArchived: Boolean): Flow<List<Project>>
}
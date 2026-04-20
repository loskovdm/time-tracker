package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Project
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectRepository {
    suspend fun addProject(
        project: Project,
        addedAt: Instant,
    )
    suspend fun updateProject(
        project: Project,
        updatedAt: Instant,
    )
    suspend fun deleteProject(
        project: Project,
        deletedAt: Instant,
    )
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getProjectById(id: Uuid): Project?
    fun observeProjects(): Flow<List<Project>>
}
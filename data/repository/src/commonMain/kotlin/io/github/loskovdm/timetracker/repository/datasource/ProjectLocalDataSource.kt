package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.Project
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectLocalDataSource {
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun archiveProject(project: Project)
    suspend fun unarchiveProject(project: Project)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getProjectById(id: Uuid): Project?
    fun getProjects(isArchived: Boolean): Flow<List<Project>>
}
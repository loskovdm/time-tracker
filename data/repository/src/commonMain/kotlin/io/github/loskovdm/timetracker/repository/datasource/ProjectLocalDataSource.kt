package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.Project
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectLocalDataSource {
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getProjectById(id: Uuid): Project?
    fun getProjects(): Flow<List<Project>>
}
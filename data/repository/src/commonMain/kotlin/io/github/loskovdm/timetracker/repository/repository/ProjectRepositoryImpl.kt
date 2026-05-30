package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.timetracker.repository.datasource.ProjectLocalDataSource
import io.github.loskovdm.timetracker.repository.mapper.ProjectMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class ProjectRepositoryImpl(
    private val localDataSource: ProjectLocalDataSource,
    private val mapper: ProjectMapper,
): ProjectRepository {
    override suspend fun addProject(project: Project) {
        localDataSource.addProject(mapper.toRepo(project))
    }

    override suspend fun updateProject(project: Project) {
        localDataSource.updateProject(mapper.toRepo(project))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getProjectById(id: Uuid): Project? {
        return localDataSource.getProjectById(id)?.let { mapper.toDomain(it) }
    }

    override fun getProjects(isArchived: Boolean): Flow<List<Project>> {
        return localDataSource.getProjects(isArchived).map { mapper.toDomain(it) }
    }

    override suspend fun deleteProject(project: Project) {
        localDataSource.deleteProject(mapper.toRepo(project))
    }
}
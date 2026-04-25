package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.timetracker.repository.datasource.ProjectLocalDataSource
import io.github.loskovdm.timetracker.repository.model.toDomain
import io.github.loskovdm.timetracker.repository.model.toRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectRepositoryImpl(
    private val localDataSource: ProjectLocalDataSource,
): ProjectRepository {
    override suspend fun addProject(
        project: Project,
        addedAt: Instant,
    ) {
        localDataSource.addProject(project.toRepo(addedAt))
    }

    override suspend fun updateProject(
        project: Project,
        updatedAt: Instant,
    ) {
        localDataSource.updateProject(project.toRepo(updatedAt))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getProjectById(id: Uuid): Project? {
        return localDataSource.getProjectById(id)?.toDomain()
    }

    override fun getProjects(): Flow<List<Project>> {
        return localDataSource.getProjects().map { repoProjects ->
            repoProjects.map { repoProject -> repoProject.toDomain() }
        }
    }
}
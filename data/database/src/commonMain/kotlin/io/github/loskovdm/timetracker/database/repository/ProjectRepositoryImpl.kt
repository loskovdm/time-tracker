package io.github.loskovdm.timetracker.database.repository

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.mapper.ProjectMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class ProjectRepositoryImpl(
    private val dao: ProjectDao,
    private val mapper: ProjectMapper,
) : ProjectRepository {
    override suspend fun addProject(project: Project) {
        dao.insertProject(mapper.toEntityForInsert(project))
    }

    override suspend fun updateProject(project: Project) {
        val existing = dao.getProjectById(project.id) ?: return
        dao.updateProject(mapper.toEntityForUpdate(project, existing))
    }

    override suspend fun deleteProject(project: Project) {
        val existing = dao.getProjectById(project.id) ?: return
        dao.deleteProject(mapper.toEntityForUpdate(project, existing))
    }

    override suspend fun getProjectById(id: Uuid): Project? {
        return dao.getProjectById(id)?.let { mapper.toDomain(it) }
    }

    override fun getProjects(isArchived: Boolean): Flow<List<Project>> {
        return dao.getProjects(isArchived).map { mapper.toDomain(it) }
    }
}
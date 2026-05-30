package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.mapper.ProjectMapper
import io.github.loskovdm.timetracker.repository.datasource.ProjectLocalDataSource
import io.github.loskovdm.timetracker.repository.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class ProjectLocalDataSourceImpl(
    private val dao: ProjectDao,
    private val mapper: ProjectMapper,
) : ProjectLocalDataSource {
    override suspend fun addProject(project: Project) {
        dao.insertProject(mapper.toEntity(project))
    }

    override suspend fun updateProject(project: Project) {
        dao.updateProject(mapper.toEntity(project))
    }

    override suspend fun getProjectById(id: Uuid): Project? {
        return dao.getProjectById(id)?.let { mapper.toRepo(it) }
    }

    override fun getProjects(isArchived: Boolean): Flow<List<Project>> {
        return dao.getProjects(isArchived).map { mapper.toRepo(it) }
    }

    override suspend fun deleteProject(project: Project) {
        dao.deleteProject(mapper.toEntity(project))
    }
}

package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.model.toEntity
import io.github.loskovdm.timetracker.database.model.toRepo
import io.github.loskovdm.timetracker.repository.datasource.ProjectLocalDataSource
import io.github.loskovdm.timetracker.repository.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class ProjectLocalDataSourceImpl(private val dao: ProjectDao): ProjectLocalDataSource {
    override suspend fun addProject(project: Project) {
        dao.insertProject(project.toEntity())
    }

    override suspend fun updateProject(project: Project) {
        dao.updateProject(project.toEntity())
    }

    override suspend fun getProjectById(id: Uuid): Project? {
        return dao.getProjectById(id)?.toRepo()
    }

    override fun getProjects(): Flow<List<Project>> {
        return dao.getProjects().map { projectEntities ->
            projectEntities.map { projectEntity -> projectEntity.toRepo() }
        }
    }
}
package io.github.loskovdm.timetracker.database.dataSource

import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.entity.toEntity
import io.github.loskovdm.timetracker.database.entity.toRepo
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

    override suspend fun deleteProject(project: Project) {
        dao.deleteProject(project.toEntity())
    }

    override suspend fun getProjectById(id: Uuid): Project? {
        return dao.getProjectById(id)?.toRepo()
    }

    override fun observeProjects(): Flow<List<Project>> {
        return dao.observeProjects().map { projectEntities ->
            projectEntities.map { projectEntity -> projectEntity.toRepo() }
        }
    }
}
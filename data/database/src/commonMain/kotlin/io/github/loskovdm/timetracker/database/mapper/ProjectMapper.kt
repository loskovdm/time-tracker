package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.timetracker.database.model.Project as EntityProject
import io.github.loskovdm.timetracker.repository.model.Project as RepoProject

internal class ProjectMapper {
    fun toEntity(
        repoProject: RepoProject,
        isArchived: Boolean = false,
    ): EntityProject =
        EntityProject(
            id = repoProject.id,
            name = repoProject.name,
            color = repoProject.color,
            isArchived = isArchived,
        )

    fun toRepo(entityProject: EntityProject): RepoProject =
        RepoProject(
            id = entityProject.id,
            name = entityProject.name,
            color = entityProject.color,
        )

    fun toRepo(entityProjects: List<EntityProject>): List<RepoProject> =
        entityProjects.map { toRepo(it) }
}
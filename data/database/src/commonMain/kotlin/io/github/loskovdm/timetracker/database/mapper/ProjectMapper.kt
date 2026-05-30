package io.github.loskovdm.timetracker.database.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.database.model.Project as EntityProject
import io.github.loskovdm.timetracker.repository.model.Project as RepoProject

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper {
    fun toEntity(repoProject: RepoProject): EntityProject {
        return EntityProject(
            id = repoProject.id.toString(),
            name = repoProject.name,
            color = repoProject.color,
            isArchived = repoProject.isArchived,
            userId = ""
        )
    }

    fun toRepo(entityProject: EntityProject): RepoProject =
        RepoProject(
            id = kotlin.uuid.Uuid.parse(entityProject.id),
            name = entityProject.name,
            color = entityProject.color,
            isArchived = entityProject.isArchived,
        )

    fun toRepo(entityProjects: List<EntityProject>): List<RepoProject> =
        entityProjects.map { toRepo(it) }
}

package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.timetracker.database.model.Project as EntityProject
import io.github.loskovdm.timetracker.repository.model.Project as RepoProject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(repoProject: RepoProject): EntityProject =
        EntityProject(
            id = repoProject.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            name = repoProject.name,
            color = repoProject.color,
            isArchived = repoProject.isArchived,
        )

    fun toEntityForUpdate(repoProject: RepoProject, existing: EntityProject): EntityProject =
        EntityProject(
            id = repoProject.id.toString(),
            userId = existing.userId,
            name = repoProject.name,
            color = repoProject.color,
            isArchived = repoProject.isArchived,
        )

    fun toEntity(repoProject: RepoProject): EntityProject = toEntityForInsert(repoProject)

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

package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.model.Project as DomainProject
import io.github.loskovdm.timetracker.database.model.Project as EntityProject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(domainProject: DomainProject): EntityProject =
        EntityProject(
            id = domainProject.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            name = domainProject.name,
            color = domainProject.color,
            isArchived = domainProject.isArchived,
        )

    fun toEntityForUpdate(domainProject: DomainProject, existing: EntityProject): EntityProject =
        EntityProject(
            id = domainProject.id.toString(),
            userId = existing.userId,
            name = domainProject.name,
            color = domainProject.color,
            isArchived = domainProject.isArchived,
        )

    fun toDomain(entityProject: EntityProject): DomainProject =
        DomainProject(
            id = kotlin.uuid.Uuid.parse(entityProject.id),
            name = entityProject.name,
            color = entityProject.color,
            isArchived = entityProject.isArchived,
        )

    fun toDomain(entityProjects: List<EntityProject>): List<DomainProject> =
        entityProjects.map { toDomain(it) }
}

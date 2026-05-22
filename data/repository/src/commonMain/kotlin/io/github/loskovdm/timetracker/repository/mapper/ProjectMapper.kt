package io.github.loskovdm.timetracker.repository.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.repository.model.Project as RepoProject
import io.github.loskovdm.domain.model.Project as DomainProject

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper {
    fun toRepo(domainProject: DomainProject): RepoProject =
        RepoProject(
            id = domainProject.id,
            name = domainProject.name,
            color = domainProject.color,
            isArchived = domainProject.isArchived,
        )

    fun toDomain(repoProject: RepoProject): DomainProject =
        DomainProject(
            id = repoProject.id,
            name = repoProject.name,
            color = repoProject.color,
            isArchived = repoProject.isArchived,
        )

    fun toDomain(repoProjects: List<RepoProject>): List<DomainProject> =
        repoProjects.map { toDomain(it) }
}
package io.github.loskovdm.timetracker.feature.projects.impl.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.feature.projects.api.model.Project as ViewProject
import io.github.loskovdm.domain.model.Project as DomainProject

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper {
    fun toView(domainProject: DomainProject): ViewProject =
        ViewProject(
            id = domainProject.id,
            name = domainProject.name,
            color = domainProject.color,
        )

    fun toView(domainProjects: List<DomainProject>): List<ViewProject> =
        domainProjects.map { toView(it) }
}
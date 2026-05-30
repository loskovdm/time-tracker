package io.github.loskovdm.timetracker.feature.reports.impl.mapper

import io.github.loskovdm.domain.model.Project as DomainProject
import io.github.loskovdm.timetracker.feature.projects.api.model.Project as ViewProject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class ProjectMapper {
    fun toView(domainProject: DomainProject): ViewProject =
        ViewProject(
            id = domainProject.id,
            name = domainProject.name,
            color = domainProject.color,
            isArchived = domainProject.isArchived,
        )
}


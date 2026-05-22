package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ArchiveProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        color: Long,
    ) {
        val project = Project(
            id = id,
            name = name,
            color = color,
            isArchived = true,
        )
        repository.updateProject(project = project)
    }
}
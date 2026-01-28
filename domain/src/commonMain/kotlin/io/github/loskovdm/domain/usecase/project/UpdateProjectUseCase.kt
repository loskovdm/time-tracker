package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository

class UpdateProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(
        id: Long,
        name: String,
        color: String,
    ) {
        val project = Project(
            id = id,
            name = name,
            color = color,
        )
        repository.updateProject(project)
    }
}
package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetProjectsUseCase(
    private val repository: ProjectRepository,
) {
    operator fun invoke(): Flow<List<Project>> {
        return repository.getProjects()
    }
}
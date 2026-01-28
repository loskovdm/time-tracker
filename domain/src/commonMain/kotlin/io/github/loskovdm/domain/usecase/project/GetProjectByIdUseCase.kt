package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: Long): Flow<Project>? {
        return repository.getProjectById(id)
    }
}
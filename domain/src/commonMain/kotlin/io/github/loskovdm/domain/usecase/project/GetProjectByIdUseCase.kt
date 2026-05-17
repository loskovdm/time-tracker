package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: Uuid): Project? {
        return repository.getProjectById(id = id)
    }
}
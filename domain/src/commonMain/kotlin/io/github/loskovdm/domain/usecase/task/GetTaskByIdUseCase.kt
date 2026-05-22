package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskByIdUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(id: Uuid): Task? {
        return repository.getTaskById(id)
    }
}
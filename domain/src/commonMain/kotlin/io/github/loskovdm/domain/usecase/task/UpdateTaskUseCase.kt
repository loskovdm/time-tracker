package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UpdateTaskUseCase(
    private val repository: TaskRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        projectId: Uuid,
    ) {
        val task = Task(
            id = id,
            name = name,
            projectId = projectId,
        )
        repository.updateTask(task = task)
    }
}
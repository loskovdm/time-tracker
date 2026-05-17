package io.github.loskovdm.domain.usecase.timeentry

import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.repository.TimeEntryWithRelationsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTimeEntryByIdUseCase(
    private val repository: TimeEntryWithRelationsRepository,
) {
    suspend operator fun invoke(id: Uuid): TimeEntryWithRelations? {
        return repository.getTimeEntryWithRelationsById(id)
    }
}
package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.SyncRepository

class StartSyncUseCase(
    private val syncRepository: SyncRepository,
) {
    suspend operator fun invoke() {
        syncRepository.connect()
    }
}

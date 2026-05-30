package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository
import io.github.loskovdm.domain.repository.GuestDataRepository
import io.github.loskovdm.domain.repository.SyncRepository

class MigrateGuestDataUseCase(
    private val guestDataRepository: GuestDataRepository,
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository,
) {
    suspend operator fun invoke() {
        val userId = authRepository.currentUserId()
            ?: error("Cannot migrate guest data without authenticated user")
        syncRepository.prepareLocalUploadPipeline()
        guestDataRepository.migrateToUser(userId)
        syncRepository.flushPendingLocalUploads()
    }
}

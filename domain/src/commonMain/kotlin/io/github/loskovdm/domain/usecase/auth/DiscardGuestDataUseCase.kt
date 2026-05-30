package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.GuestDataRepository
import io.github.loskovdm.domain.repository.SyncRepository

class DiscardGuestDataUseCase(
    private val guestDataRepository: GuestDataRepository,
    private val syncRepository: SyncRepository,
) {
    suspend operator fun invoke() {
        guestDataRepository.clearAll()
        runCatching { syncRepository.clearPendingUploads() }
    }
}

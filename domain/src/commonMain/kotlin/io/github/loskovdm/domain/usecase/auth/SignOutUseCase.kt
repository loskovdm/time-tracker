package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository
import io.github.loskovdm.domain.repository.GuestDataRepository
import io.github.loskovdm.domain.repository.SyncRepository

class SignOutUseCase(
    private val syncRepository: SyncRepository,
    private val guestDataRepository: GuestDataRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        runCatching { syncRepository.disconnect() }
        runCatching { authRepository.signOut() }
        guestDataRepository.clearAll()
        // clearAll() enqueues DELETEs via PowerSync triggers; discard them so the next
        // sign-in does not delete the user's server data.
        runCatching { syncRepository.clearPendingUploads() }
    }
}

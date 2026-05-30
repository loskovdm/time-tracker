package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.GuestDataRepository

class HasGuestLocalDataUseCase(
    private val guestDataRepository: GuestDataRepository,
) {
    suspend operator fun invoke(): Boolean = guestDataRepository.hasGuestData()
}

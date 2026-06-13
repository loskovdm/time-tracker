package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class VerifyRecoveryOtpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, token: String) {
        authRepository.verifyRecoveryOtp(email.trim(), token.trim())
    }
}

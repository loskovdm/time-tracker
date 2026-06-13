package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class VerifySignupOtpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, token: String) {
        authRepository.verifySignupOtp(email.trim(), token.trim())
    }
}

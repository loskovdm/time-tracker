package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.model.AuthState
import io.github.loskovdm.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthState> = authRepository.observeAuthState()
}

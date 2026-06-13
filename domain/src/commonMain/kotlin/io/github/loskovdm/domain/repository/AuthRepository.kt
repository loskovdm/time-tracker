package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String)

    suspend fun verifySignupOtp(email: String, token: String)

    suspend fun resendSignupConfirmation(email: String)

    suspend fun requestPasswordReset(email: String)

    suspend fun verifyRecoveryOtp(email: String, token: String)

    suspend fun resendPasswordReset(email: String)

    suspend fun updatePassword(newPassword: String)

    suspend fun signOut()

    fun currentUserId(): String?
}

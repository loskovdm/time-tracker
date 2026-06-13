package io.github.loskovdm.timetracker.supabaseclient

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.loskovdm.domain.model.AuthException
import io.github.loskovdm.domain.model.AuthState
import io.github.loskovdm.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val client: SupabaseClient,
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> =
        client.auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val session = status.session
                    val email = session.user?.email.orEmpty()
                    val userId = session.user?.id.orEmpty()
                    if (userId.isNotEmpty()) {
                        AuthState.Authenticated(email = email, userId = userId)
                    } else {
                        AuthState.Guest
                    }
                }

                is SessionStatus.NotAuthenticated -> AuthState.Guest

                is SessionStatus.Initializing -> AuthState.Loading

                is SessionStatus.RefreshFailure -> AuthState.Guest
            }
        }

    override suspend fun signIn(email: String, password: String) {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun signUp(email: String, password: String) {
        try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun verifySignupOtp(email: String, token: String) {
        try {
            client.auth.verifyEmailOtp(
                type = OtpType.Email.SIGNUP,
                email = email,
                token = token,
            )
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun resendSignupConfirmation(email: String) {
        try {
            client.auth.resendEmail(OtpType.Email.SIGNUP, email)
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun requestPasswordReset(email: String) {
        try {
            client.auth.resetPasswordForEmail(email = email)
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun verifyRecoveryOtp(email: String, token: String) {
        try {
            client.auth.verifyEmailOtp(
                type = OtpType.Email.RECOVERY,
                email = email,
                token = token,
            )
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun resendPasswordReset(email: String) {
        requestPasswordReset(email)
    }

    override suspend fun updatePassword(newPassword: String) {
        try {
            client.auth.updateUser {
                password = newPassword
            }
        } catch (e: AuthException) {
            throw e
        } catch (e: Throwable) {
            throw SupabaseAuthErrorMapper.map(e)
        }
    }

    override suspend fun signOut() {
        // Local scope clears the session even when the server session is already invalid.
        runCatching {
            client.auth.signOut(SignOutScope.LOCAL)
        }
    }

    override fun currentUserId(): String? =
        client.auth.currentSessionOrNull()?.user?.id
}

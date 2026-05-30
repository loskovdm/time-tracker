package io.github.loskovdm.timetracker.feature.auth.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.model.AuthError
import io.github.loskovdm.domain.model.AuthException
import io.github.loskovdm.domain.usecase.auth.SignInUseCase
import io.github.loskovdm.domain.usecase.auth.SignUpUseCase
import io.github.loskovdm.domain.util.isValidEmail
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarMode
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal enum class AuthMode {
    SignIn,
    SignUp,
}

internal sealed interface AuthDialogState {
    data class Error(val error: AuthError) : AuthDialogState

    data class RegistrationEmailSent(val email: String) : AuthDialogState
}

internal data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val emailInvalid: Boolean = false,
    val isLoading: Boolean = false,
    val mode: AuthMode = AuthMode.SignIn,
    val dialog: AuthDialogState? = null,
)

internal class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val authTopBarModeSource: AuthTopBarModeSource,
    private val authNavigationLock: AuthNavigationLock,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _finishAuth = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishAuth: SharedFlow<Unit> = _finishAuth.asSharedFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailInvalid = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onToggleMode() {
        if (authNavigationLock.isBlockingBack.value) return
        val newMode = when (_uiState.value.mode) {
            AuthMode.SignIn -> AuthMode.SignUp
            AuthMode.SignUp -> AuthMode.SignIn
        }
        _uiState.update { it.copy(mode = newMode, dialog = null) }
        authTopBarModeSource.setMode(
            when (newMode) {
                AuthMode.SignIn -> AuthTopBarMode.SignIn
                AuthMode.SignUp -> AuthTopBarMode.SignUp
            },
        )
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(dialog = null) }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (!isValidEmail(state.email)) {
            _uiState.update { it.copy(emailInvalid = true) }
            return
        }
        viewModelScope.launch {
            authNavigationLock.begin()
            _uiState.update { it.copy(isLoading = true, dialog = null) }
            try {
                when (state.mode) {
                    AuthMode.SignIn -> {
                        signInUseCase(state.email, state.password)
                        _uiState.update { it.copy(isLoading = false) }
                        _finishAuth.emit(Unit)
                    }
                    AuthMode.SignUp -> {
                        signUpUseCase(state.email, state.password)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                dialog = AuthDialogState.RegistrationEmailSent(state.email),
                            )
                        }
                    }
                }
            } catch (e: AuthException) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = AuthDialogState.Error(e.error))
                }
            } catch (_: Throwable) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = AuthDialogState.Error(AuthError.Generic))
                }
            } finally {
                authNavigationLock.end()
            }
        }
    }
}

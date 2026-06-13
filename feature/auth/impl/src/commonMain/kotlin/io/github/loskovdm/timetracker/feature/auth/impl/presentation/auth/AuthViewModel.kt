package io.github.loskovdm.timetracker.feature.auth.impl.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.model.AuthError
import io.github.loskovdm.domain.model.AuthException
import io.github.loskovdm.domain.usecase.auth.ResendSignupConfirmationUseCase
import io.github.loskovdm.domain.usecase.auth.SignInUseCase
import io.github.loskovdm.domain.usecase.auth.SignUpUseCase
import io.github.loskovdm.domain.usecase.auth.VerifySignupOtpUseCase
import io.github.loskovdm.domain.util.OTP_CODE_LENGTH
import io.github.loskovdm.domain.util.isValidEmail
import io.github.loskovdm.domain.util.isValidOtp
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

internal enum class AuthStep {
    Credentials,
    VerifySignupOtp,
}

internal sealed interface AuthDialogState {
    data class Error(val error: AuthError) : AuthDialogState

    data object ConfirmationCodeSent : AuthDialogState
}

internal data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val otpCode: String = "",
    val emailInvalid: Boolean = false,
    val otpInvalid: Boolean = false,
    val isLoading: Boolean = false,
    val mode: AuthMode = AuthMode.SignIn,
    val step: AuthStep = AuthStep.Credentials,
    val dialog: AuthDialogState? = null,
)

internal class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val verifySignupOtpUseCase: VerifySignupOtpUseCase,
    private val resendSignupConfirmationUseCase: ResendSignupConfirmationUseCase,
    private val authTopBarModeSource: AuthTopBarModeSource,
    private val authNavigationLock: AuthNavigationLock,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _finishAuth = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishAuth: SharedFlow<Unit> = _finishAuth.asSharedFlow()

    init {
        authTopBarModeSource.setBackInterceptor { onBackFromInternalStep() }
        syncTopBarMode(_uiState.value)
    }

    override fun onCleared() {
        authTopBarModeSource.setBackInterceptor(null)
        super.onCleared()
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailInvalid = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onOtpCodeChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }.take(OTP_CODE_LENGTH)
        _uiState.update { it.copy(otpCode = digitsOnly, otpInvalid = false) }
    }

    fun onToggleMode() {
        if (authNavigationLock.isBlockingBack.value) return

        val newMode = when (_uiState.value.mode) {
            AuthMode.SignIn -> AuthMode.SignUp
            AuthMode.SignUp -> AuthMode.SignIn
        }
        _uiState.update {
            it.copy(
                mode = newMode,
                step = AuthStep.Credentials,
                dialog = null,
                otpCode = "",
                otpInvalid = false,
            )
        }
        syncTopBarMode(_uiState.value)
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(dialog = null) }
    }

    fun onSubmit() {
        when (_uiState.value.step) {
            AuthStep.Credentials -> onSubmitCredentials()
            AuthStep.VerifySignupOtp -> onSubmitOtp()
        }
    }

    fun onResendConfirmationCode() {
        val email = _uiState.value.email
        if (!isValidEmail(email)) {
            _uiState.update { it.copy(emailInvalid = true) }
            return
        }

        viewModelScope.launch {
            authNavigationLock.begin()
            _uiState.update { it.copy(isLoading = true, dialog = null) }
            try {
                resendSignupConfirmationUseCase(email)
                _uiState.update {
                    it.copy(isLoading = false, dialog = AuthDialogState.ConfirmationCodeSent)
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

    private fun onSubmitCredentials() {
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
                                step = AuthStep.VerifySignupOtp,
                                otpCode = "",
                                otpInvalid = false,
                            )
                        }
                        syncTopBarMode(_uiState.value)
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

    private fun onSubmitOtp() {
        val state = _uiState.value
        if (!isValidOtp(state.otpCode)) {
            _uiState.update { it.copy(otpInvalid = true) }
            return
        }

        viewModelScope.launch {
            authNavigationLock.begin()
            _uiState.update { it.copy(isLoading = true, dialog = null) }
            try {
                verifySignupOtpUseCase(state.email, state.otpCode)
                _uiState.update { it.copy(isLoading = false) }
                _finishAuth.emit(Unit)
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

    private fun onBackFromInternalStep(): Boolean {
        if (_uiState.value.step != AuthStep.VerifySignupOtp) return false
        if (authNavigationLock.isBlockingBack.value) return true

        _uiState.update {
            it.copy(
                step = AuthStep.Credentials,
                otpCode = "",
                otpInvalid = false,
                dialog = null,
            )
        }
        syncTopBarMode(_uiState.value)
        return true
    }

    private fun syncTopBarMode(state: AuthUiState) {
        authTopBarModeSource.setMode(
            when {
                state.step == AuthStep.VerifySignupOtp -> AuthTopBarMode.VerifyEmail
                state.mode == AuthMode.SignIn -> AuthTopBarMode.SignIn
                else -> AuthTopBarMode.SignUp
            },
        )
    }
}

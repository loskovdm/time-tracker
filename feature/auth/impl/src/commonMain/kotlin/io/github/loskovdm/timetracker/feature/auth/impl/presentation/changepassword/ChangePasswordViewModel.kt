package io.github.loskovdm.timetracker.feature.auth.impl.presentation.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.model.AuthError
import io.github.loskovdm.domain.model.AuthException
import io.github.loskovdm.domain.usecase.auth.RequestPasswordResetUseCase
import io.github.loskovdm.domain.usecase.auth.ResendPasswordResetUseCase
import io.github.loskovdm.domain.usecase.auth.UpdatePasswordUseCase
import io.github.loskovdm.domain.usecase.auth.VerifyRecoveryOtpUseCase
import io.github.loskovdm.domain.util.OTP_CODE_LENGTH
import io.github.loskovdm.domain.util.isValidEmail
import io.github.loskovdm.domain.util.isValidOtp
import io.github.loskovdm.domain.util.isValidPassword
import io.github.loskovdm.timetracker.feature.auth.api.ChangePasswordDestination
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarMode
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal enum class ChangePasswordStep {
    RequestEmail,
    VerifyOtp,
    SetNewPassword,
}

internal sealed interface ChangePasswordDialogState {
    data class Error(val error: AuthError) : ChangePasswordDialogState

    data object ResetCodeSent : ChangePasswordDialogState

    data object PasswordUpdated : ChangePasswordDialogState
}

internal data class ChangePasswordUiState(
    val email: String = "",
    val otpCode: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val emailInvalid: Boolean = false,
    val otpInvalid: Boolean = false,
    val passwordInvalid: Boolean = false,
    val passwordMismatch: Boolean = false,
    val isLoading: Boolean = false,
    val step: ChangePasswordStep = ChangePasswordStep.RequestEmail,
    val isChangePasswordFlow: Boolean = false,
    val closeAuthOnSuccess: Boolean = false,
    val dialog: ChangePasswordDialogState? = null,
)

internal class ChangePasswordViewModel(
    destination: ChangePasswordDestination,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val verifyRecoveryOtpUseCase: VerifyRecoveryOtpUseCase,
    private val resendPasswordResetUseCase: ResendPasswordResetUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val changePasswordTopBarSource: ChangePasswordTopBarSource,
    private val authNavigationLock: AuthNavigationLock,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChangePasswordUiState(
            email = destination.prefilledEmail.orEmpty(),
            isChangePasswordFlow = destination.prefilledEmail != null,
            closeAuthOnSuccess = destination.closeAuthOnSuccess,
            step = if (destination.prefilledEmail != null) {
                ChangePasswordStep.VerifyOtp
            } else {
                ChangePasswordStep.RequestEmail
            },
            isLoading = destination.prefilledEmail != null,
        ),
    )
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _finish = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finish: SharedFlow<Unit> = _finish.asSharedFlow()

    init {
        changePasswordTopBarSource.setBackInterceptor { onBackFromInternalStep() }
        syncTopBarMode(_uiState.value)

        if (destination.prefilledEmail != null) {
            sendResetCode(showConfirmationDialog = false)
        }
    }

    override fun onCleared() {
        changePasswordTopBarSource.setBackInterceptor(null)
        super.onCleared()
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailInvalid = false) }
    }

    fun onOtpCodeChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }.take(OTP_CODE_LENGTH)
        _uiState.update { it.copy(otpCode = digitsOnly, otpInvalid = false) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                passwordInvalid = false,
                passwordMismatch = false,
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                passwordInvalid = false,
                passwordMismatch = false,
            )
        }
    }

    fun onDismissDialog() {
        val dialog = _uiState.value.dialog
        _uiState.update { it.copy(dialog = null) }
        if (dialog is ChangePasswordDialogState.PasswordUpdated) {
            viewModelScope.launch { _finish.emit(Unit) }
        }
    }

    fun onSubmit() {
        when (_uiState.value.step) {
            ChangePasswordStep.RequestEmail -> sendResetCode(showConfirmationDialog = true)
            ChangePasswordStep.VerifyOtp -> verifyOtp()
            ChangePasswordStep.SetNewPassword -> updatePassword()
        }
    }

    fun onResendCode() {
        sendResetCode(showConfirmationDialog = true)
    }

    private fun sendResetCode(showConfirmationDialog: Boolean) {
        val email = _uiState.value.email
        if (!isValidEmail(email)) {
            _uiState.update { it.copy(emailInvalid = true) }
            return
        }

        viewModelScope.launch {
            authNavigationLock.begin()
            _uiState.update { it.copy(isLoading = true, dialog = null) }
            try {
                requestPasswordResetUseCase(email)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        step = ChangePasswordStep.VerifyOtp,
                        otpCode = "",
                        otpInvalid = false,
                        dialog = if (showConfirmationDialog) {
                            ChangePasswordDialogState.ResetCodeSent
                        } else {
                            null
                        },
                    )
                }
                syncTopBarMode(_uiState.value)
            } catch (e: AuthException) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(e.error))
                }
            } catch (_: Throwable) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(AuthError.Generic))
                }
            } finally {
                authNavigationLock.end()
            }
        }
    }

    private fun verifyOtp() {
        val state = _uiState.value
        if (!isValidOtp(state.otpCode)) {
            _uiState.update { it.copy(otpInvalid = true) }
            return
        }

        viewModelScope.launch {
            authNavigationLock.begin()
            _uiState.update { it.copy(isLoading = true, dialog = null) }
            try {
                verifyRecoveryOtpUseCase(state.email, state.otpCode)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        step = ChangePasswordStep.SetNewPassword,
                        newPassword = "",
                        confirmPassword = "",
                        passwordInvalid = false,
                        passwordMismatch = false,
                    )
                }
                syncTopBarMode(_uiState.value)
            } catch (e: AuthException) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(e.error))
                }
            } catch (_: Throwable) {
                _uiState.update {
                    it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(AuthError.Generic))
                }
            } finally {
                authNavigationLock.end()
            }
        }
    }

    private fun updatePassword() {
        val state = _uiState.value
        when {
            !isValidPassword(state.newPassword) -> {
                _uiState.update { it.copy(passwordInvalid = true) }
            }

            state.newPassword != state.confirmPassword -> {
                _uiState.update { it.copy(passwordMismatch = true) }
            }

            else -> viewModelScope.launch {
                authNavigationLock.begin()
                _uiState.update { it.copy(isLoading = true, dialog = null) }
                try {
                    updatePasswordUseCase(state.newPassword)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            dialog = ChangePasswordDialogState.PasswordUpdated,
                        )
                    }
                } catch (e: AuthException) {
                    _uiState.update {
                        it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(e.error))
                    }
                } catch (_: Throwable) {
                    _uiState.update {
                        it.copy(isLoading = false, dialog = ChangePasswordDialogState.Error(AuthError.Generic))
                    }
                } finally {
                    authNavigationLock.end()
                }
            }
        }
    }

    private fun onBackFromInternalStep(): Boolean {
        if (authNavigationLock.isBlockingBack.value) return true

        return when (_uiState.value.step) {
            ChangePasswordStep.SetNewPassword -> {
                _uiState.update {
                    it.copy(
                        step = ChangePasswordStep.VerifyOtp,
                        newPassword = "",
                        confirmPassword = "",
                        passwordInvalid = false,
                        passwordMismatch = false,
                        dialog = null,
                    )
                }
                syncTopBarMode(_uiState.value)
                true
            }

            ChangePasswordStep.VerifyOtp -> {
                if (_uiState.value.isChangePasswordFlow) {
                    false
                } else {
                    _uiState.update {
                        it.copy(
                            step = ChangePasswordStep.RequestEmail,
                            otpCode = "",
                            otpInvalid = false,
                            dialog = null,
                        )
                    }
                    syncTopBarMode(_uiState.value)
                    true
                }
            }

            ChangePasswordStep.RequestEmail -> false
        }
    }

    private fun syncTopBarMode(state: ChangePasswordUiState) {
        changePasswordTopBarSource.setMode(
            when (state.step) {
                ChangePasswordStep.RequestEmail -> {
                    if (state.isChangePasswordFlow) {
                        ChangePasswordTopBarMode.ChangePassword
                    } else {
                        ChangePasswordTopBarMode.ResetPassword
                    }
                }

                ChangePasswordStep.VerifyOtp -> ChangePasswordTopBarMode.VerifyCode
                ChangePasswordStep.SetNewPassword -> ChangePasswordTopBarMode.NewPassword
            },
        )
    }
}

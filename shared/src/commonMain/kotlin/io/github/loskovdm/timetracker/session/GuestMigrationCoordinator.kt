package io.github.loskovdm.timetracker.session

import io.github.loskovdm.domain.model.AuthState
import io.github.loskovdm.domain.usecase.auth.DiscardGuestDataUseCase
import io.github.loskovdm.domain.usecase.auth.HasGuestLocalDataUseCase
import io.github.loskovdm.domain.usecase.auth.MigrateGuestDataUseCase
import io.github.loskovdm.domain.usecase.auth.ObserveAuthStateUseCase
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Prompts to migrate or discard guest data after sign-in (including restored sessions)
 * and blocks sync until the user chooses.
 */
class GuestMigrationCoordinator(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val hasGuestLocalDataUseCase: HasGuestLocalDataUseCase,
    private val migrateGuestDataUseCase: MigrateGuestDataUseCase,
    private val discardGuestDataUseCase: DiscardGuestDataUseCase,
    private val authNavigationLock: AuthNavigationLock,
    private val scope: CoroutineScope,
) {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _syncAllowed = MutableStateFlow(false)
    val syncAllowed: StateFlow<Boolean> = _syncAllowed.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    init {
        scope.launch {
            observeAuthStateUseCase()
                .distinctUntilChanged()
                .collect { state ->
                    when (state) {
                        is AuthState.Authenticated -> evaluateAuthenticatedSession()
                        else -> {
                            _showDialog.value = false
                            _syncAllowed.value = false
                        }
                    }
                }
        }
    }

    private suspend fun evaluateAuthenticatedSession() {
        if (hasGuestLocalDataUseCase()) {
            _syncAllowed.value = false
            _showDialog.value = true
            authNavigationLock.begin()
        } else {
            _showDialog.value = false
            _syncAllowed.value = true
        }
    }

    fun onMigrate() {
        if (_isProcessing.value) return
        scope.launch {
            _isProcessing.value = true
            try {
                migrateGuestDataUseCase()
                _showDialog.value = false
                _syncAllowed.value = true
            } finally {
                _isProcessing.value = false
                authNavigationLock.end()
            }
        }
    }

    fun onDiscard() {
        if (_isProcessing.value) return
        scope.launch {
            _isProcessing.value = true
            try {
                discardGuestDataUseCase()
                _showDialog.value = false
                _syncAllowed.value = true
            } finally {
                _isProcessing.value = false
                authNavigationLock.end()
            }
        }
    }
}

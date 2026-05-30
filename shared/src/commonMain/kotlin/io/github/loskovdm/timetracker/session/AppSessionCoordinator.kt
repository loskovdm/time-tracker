package io.github.loskovdm.timetracker.session

import io.github.loskovdm.domain.model.AuthState
import io.github.loskovdm.domain.repository.SyncRepository
import io.github.loskovdm.domain.usecase.auth.ObserveAuthStateUseCase
import io.github.loskovdm.domain.usecase.auth.StartSyncUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Starts PowerSync when the user is authenticated and guest migration (if any) is resolved.
 */
class AppSessionCoordinator(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    guestMigrationCoordinator: GuestMigrationCoordinator,
    private val startSyncUseCase: StartSyncUseCase,
    private val syncRepository: SyncRepository,
    scope: CoroutineScope,
) {
    init {
        scope.launch {
            combine(
                observeAuthStateUseCase(),
                guestMigrationCoordinator.syncAllowed,
            ) { authState, syncAllowed ->
                authState is AuthState.Authenticated && syncAllowed
            }
                .distinctUntilChanged()
                .collect { shouldSync ->
                    if (shouldSync) {
                        runCatching { startSyncUseCase() }
                    } else {
                        runCatching { syncRepository.disconnect() }
                    }
                }
        }
    }
}

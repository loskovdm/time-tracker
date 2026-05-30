package io.github.loskovdm.timetracker.feature.settings.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.AuthState
import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.usecase.auth.ObserveAuthStateUseCase
import io.github.loskovdm.domain.usecase.auth.SignOutUseCase
import io.github.loskovdm.domain.usecase.settings.ObserveAppLanguageUseCase
import io.github.loskovdm.domain.usecase.settings.ObserveThemeModeUseCase
import io.github.loskovdm.domain.usecase.settings.SetAppLanguageUseCase
import io.github.loskovdm.domain.usecase.settings.SetThemeModeUseCase
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    observeThemeModeUseCase: ObserveThemeModeUseCase,
    observeAppLanguageUseCase: ObserveAppLanguageUseCase,
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authNavigationLock: AuthNavigationLock,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = observeThemeModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.SYSTEM)

    val appLanguage: StateFlow<AppLanguage> = observeAppLanguageUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppLanguage.ENGLISH)

    val authState: StateFlow<AuthState> = observeAuthStateUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)

    val isAuthOperationInProgress: StateFlow<Boolean> = authNavigationLock.isBlockingBack

    fun onThemeModeSelected(mode: ThemeMode) {
        viewModelScope.launch { setThemeModeUseCase(mode) }
    }

    fun onAppLanguageSelected(language: AppLanguage) {
        viewModelScope.launch { setAppLanguageUseCase(language) }
    }

    fun onSignOut() {
        if (authNavigationLock.isBlockingBack.value) return
        viewModelScope.launch {
            authNavigationLock.begin()
            try {
                runCatching { signOutUseCase() }
            } finally {
                authNavigationLock.end()
            }
        }
    }
}

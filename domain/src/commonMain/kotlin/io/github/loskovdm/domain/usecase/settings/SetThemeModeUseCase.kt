package io.github.loskovdm.domain.usecase.settings

import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.repository.UserSettingsRepository

class SetThemeModeUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(mode: ThemeMode) {
        repository.setThemeMode(mode)
    }
}

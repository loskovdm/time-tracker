package io.github.loskovdm.domain.usecase.settings

import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveThemeModeUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<ThemeMode> = repository.themeMode
}

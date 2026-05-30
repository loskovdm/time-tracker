package io.github.loskovdm.domain.usecase.settings

import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveAppLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<AppLanguage> = repository.appLanguage
}

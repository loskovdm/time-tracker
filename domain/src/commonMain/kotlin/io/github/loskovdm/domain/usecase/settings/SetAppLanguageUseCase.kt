package io.github.loskovdm.domain.usecase.settings

import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.repository.UserSettingsRepository

class SetAppLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(language: AppLanguage) {
        repository.setAppLanguage(language)
    }
}

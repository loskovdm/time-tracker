package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val themeMode: Flow<ThemeMode>
    val appLanguage: Flow<AppLanguage>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setAppLanguage(language: AppLanguage)
}

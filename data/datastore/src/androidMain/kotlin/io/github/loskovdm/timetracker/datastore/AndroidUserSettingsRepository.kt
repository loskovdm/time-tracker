package io.github.loskovdm.timetracker.datastore

import android.content.Context
import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.repository.UserSettingsRepository
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Locale

internal class AndroidUserSettingsRepository(
    private val context: Context,
) : UserSettingsRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dataStore = PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
    )

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .map { preferences ->
            preferences[THEME_MODE_KEY]?.let { stored ->
                runCatching { ThemeMode.valueOf(stored) }.getOrNull()
            } ?: ThemeMode.SYSTEM
        }
        .distinctUntilChanged()

    override val appLanguage: Flow<AppLanguage> = dataStore.data
        .map { preferences ->
            AppLanguage.fromStoredValue(
                stored = preferences[LANGUAGE_KEY],
                systemLanguageTag = systemLanguageTag(),
            )
        }
        .distinctUntilChanged()

    private fun systemLanguageTag(): String {
        return context.applicationContext.resources.configuration.locales[0]?.language
            ?: Locale.getDefault().language
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    override suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
        }
    }

    private companion object {
        const val DATASTORE_NAME = "user_settings"
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }
}

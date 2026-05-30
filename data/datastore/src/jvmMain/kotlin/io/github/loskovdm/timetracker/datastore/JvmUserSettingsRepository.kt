package io.github.loskovdm.timetracker.datastore

import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import java.util.Properties
import kotlin.io.path.notExists

internal class JvmUserSettingsRepository : UserSettingsRepository {
    private val mutex = Mutex()
    private val settingsFile: Path = Path.of(
        System.getProperty("user.home"),
        ".timetracker",
        "settings.properties",
    )

    private val themeState = MutableStateFlow(ThemeMode.SYSTEM)
    private val languageState = MutableStateFlow(
        AppLanguage.fromSystemLanguageTag(Locale.getDefault().language),
    )

    init {
        loadFromDisk()
    }

    override val themeMode: Flow<ThemeMode> = themeState.asStateFlow()
    override val appLanguage: Flow<AppLanguage> = languageState.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        mutex.withLock {
            themeState.value = mode
            persist()
        }
    }

    override suspend fun setAppLanguage(language: AppLanguage) {
        mutex.withLock {
            languageState.value = language
            persist()
        }
    }

    private fun loadFromDisk() {
        if (settingsFile.notExists()) return

        val properties = Properties()
        settingsFile.toFile().inputStream().use { properties.load(it) }

        properties.getProperty(THEME_MODE_KEY)?.let { stored ->
            runCatching { ThemeMode.valueOf(stored) }.getOrNull()
        }?.let { themeState.value = it }

        languageState.value = AppLanguage.fromStoredValue(
            stored = properties.getProperty(LANGUAGE_KEY),
            systemLanguageTag = Locale.getDefault().language,
        )
    }

    private fun persist() {
        val parent = settingsFile.parent
        if (parent != null && parent.notExists()) {
            Files.createDirectories(parent)
        }

        val properties = Properties().apply {
            setProperty(THEME_MODE_KEY, themeState.value.name)
            setProperty(LANGUAGE_KEY, languageState.value.name)
        }
        settingsFile.toFile().outputStream().use { properties.store(it, "TimeTracker settings") }
    }

    private companion object {
        const val THEME_MODE_KEY = "theme_mode"
        const val LANGUAGE_KEY = "app_language"
    }
}

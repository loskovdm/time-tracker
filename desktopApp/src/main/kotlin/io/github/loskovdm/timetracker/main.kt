package io.github.loskovdm.timetracker

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import io.github.loskovdm.designsystem.locale.applyPlatformLocale
import io.github.loskovdm.domain.repository.UserSettingsRepository
import io.github.loskovdm.timetracker.config.loadRemoteConfig
import io.github.loskovdm.timetracker.di.initKoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext

fun main() {
    initKoin(remoteConfig = loadRemoteConfig())
    runBlocking {
        val localeTag = GlobalContext.get()
            .get<UserSettingsRepository>()
            .appLanguage
            .first()
            .tag
        applyPlatformLocale(localeTag)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "TimeTracker",
        ) {
            val lifecycleOwner = rememberLifecycleOwner()
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                TimeTrackerApp()
            }
        }
    }
}

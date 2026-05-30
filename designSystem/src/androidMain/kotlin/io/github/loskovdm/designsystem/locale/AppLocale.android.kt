package io.github.loskovdm.designsystem.locale

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import java.util.Locale

@Composable
actual fun ProvideAppLocale(
    localeTag: String,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    SideEffect {
        applyPlatformLocale(localeTag)
    }

    val localeConfiguration = remember(configuration, localeTag) {
        Configuration(configuration).apply {
            setLocale(Locale.forLanguageTag(localeTag))
        }
    }

    val locale = remember(localeTag) {
        Locale.forLanguageTag(localeTag)
    }

    Locale.setDefault(locale)
    context.resources.updateConfiguration(
        localeConfiguration,
        context.resources.displayMetrics,
    )

    CompositionLocalProvider(LocalConfiguration provides localeConfiguration) {
        content()
    }
}

actual fun applyPlatformLocale(localeTag: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeTag))
}

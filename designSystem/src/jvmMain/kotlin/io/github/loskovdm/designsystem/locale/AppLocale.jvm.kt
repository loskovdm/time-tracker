package io.github.loskovdm.designsystem.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
actual fun ProvideAppLocale(
    localeTag: String,
    content: @Composable () -> Unit,
) {
    remember(localeTag) {
        applyPlatformLocale(localeTag)
    }

    content()
}

actual fun applyPlatformLocale(localeTag: String) {
    Locale.setDefault(Locale.forLanguageTag(localeTag))
}

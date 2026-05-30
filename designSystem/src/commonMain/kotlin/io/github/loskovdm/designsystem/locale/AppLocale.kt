package io.github.loskovdm.designsystem.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun AppLocaleProvider(
    localeTag: String,
    content: @Composable () -> Unit,
) {
    ProvideAppLocale(localeTag = localeTag) {
        key(localeTag) {
            content()
        }
    }
}

@Composable
expect fun ProvideAppLocale(
    localeTag: String,
    content: @Composable () -> Unit,
)

expect fun applyPlatformLocale(localeTag: String)

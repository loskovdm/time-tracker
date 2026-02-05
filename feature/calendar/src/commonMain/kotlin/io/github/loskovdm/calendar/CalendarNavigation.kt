package io.github.loskovdm.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Composable
fun CalendarNavigation(
    modifier: Modifier = Modifier,
    appBottomBar: @Composable () -> Unit,
    titleResource: StringResource,
    settingsIconResource: DrawableResource,
    onSettings: () -> Unit,
) {
    CalendarScreen(
        modifier = modifier,
        appBottomBar = {
            appBottomBar()
        },
        titleResource = titleResource,
        settingsIconResource = settingsIconResource,
        onSettings = {
            onSettings()
        },
    )
}
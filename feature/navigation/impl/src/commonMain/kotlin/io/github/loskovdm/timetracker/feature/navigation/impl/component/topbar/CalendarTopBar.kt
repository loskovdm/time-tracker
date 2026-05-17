package io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_settings_filled
import timetracker.designsystem.generated.resources.ic_settings_outlined
import timetracker.designsystem.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onSettings: () -> Unit,
) {
    val deviceConfiguration = LocalDeviceConfiguration.current

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = { Text("Calendar") },
        actions = {
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings_filled),
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}
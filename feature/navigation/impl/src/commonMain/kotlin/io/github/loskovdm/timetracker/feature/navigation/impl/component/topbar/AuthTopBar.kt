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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarMode
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_title_sign_in
import timetracker.designsystem.generated.resources.auth_title_sign_up
import timetracker.designsystem.generated.resources.auth_title_verify_email
import timetracker.designsystem.generated.resources.back
import timetracker.designsystem.generated.resources.ic_arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(
    authTopBarModeSource: AuthTopBarModeSource,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
    backEnabled: Boolean = true,
) {
    val mode by authTopBarModeSource.mode.collectAsStateWithLifecycle()
    val deviceConfiguration = LocalDeviceConfiguration.current
    val titleRes = when (mode) {
        AuthTopBarMode.SignIn -> Res.string.auth_title_sign_in
        AuthTopBarMode.SignUp -> Res.string.auth_title_sign_up
        AuthTopBarMode.VerifyEmail -> Res.string.auth_title_verify_email
    }

    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(titleRes)) },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        navigationIcon = {
            IconButton(
                onClick = onBack,
                enabled = backEnabled,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.back),
                )
            }
        },
    )
}

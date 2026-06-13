package io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.TooltipIconButton
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarMode
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarSource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_title_change_password
import timetracker.designsystem.generated.resources.auth_title_new_password
import timetracker.designsystem.generated.resources.auth_title_reset_password
import timetracker.designsystem.generated.resources.auth_title_verify_code
import timetracker.designsystem.generated.resources.back
import timetracker.designsystem.generated.resources.ic_arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordTopBar(
    changePasswordTopBarSource: ChangePasswordTopBarSource,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
    backEnabled: Boolean = true,
) {
    val mode by changePasswordTopBarSource.mode.collectAsStateWithLifecycle()
    val deviceConfiguration = LocalDeviceConfiguration.current
    val titleRes = when (mode) {
        ChangePasswordTopBarMode.ResetPassword -> Res.string.auth_title_reset_password
        ChangePasswordTopBarMode.ChangePassword -> Res.string.auth_title_change_password
        ChangePasswordTopBarMode.VerifyCode -> Res.string.auth_title_verify_code
        ChangePasswordTopBarMode.NewPassword -> Res.string.auth_title_new_password
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
            TooltipIconButton(
                onClick = onBack,
                enabled = backEnabled,
                tooltip = stringResource(Res.string.back),
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.back),
                )
            }
        },
    )
}

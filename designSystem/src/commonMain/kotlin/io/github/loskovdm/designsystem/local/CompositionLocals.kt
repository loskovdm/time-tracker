package io.github.loskovdm.designsystem.local

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.util.DeviceConfiguration

val LocalDeviceConfiguration = staticCompositionLocalOf<DeviceConfiguration> {
    error("No DeviceConfiguration provided")
}
val LocalFabPadding = compositionLocalOf { PaddingValues(bottom = 0.dp) }
package io.github.loskovdm.timetracker.feature.navigation.impl.util

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val isWidthExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            )
            val isWidthMedium = windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
            )
            val isHeightExpanded = windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
            )
            val isHeightMedium = windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
            )

            return when {
                isWidthExpanded && isHeightExpanded -> DeviceConfiguration.DESKTOP
                isWidthExpanded && isHeightMedium -> DeviceConfiguration.TABLET_LANDSCAPE
                isWidthMedium && isHeightExpanded -> DeviceConfiguration.TABLET_PORTRAIT
                isWidthMedium && !isHeightMedium -> DeviceConfiguration.MOBILE_LANDSCAPE
                else -> DeviceConfiguration.MOBILE_PORTRAIT
            }
        }
    }
}
package io.github.loskovdm.designsystem

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
                WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
            )
            val isWidthMedium = windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
            )
            val isHeightExpanded = windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
            )
            val isHeightMedium = windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
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
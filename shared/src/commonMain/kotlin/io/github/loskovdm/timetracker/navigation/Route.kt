package io.github.loskovdm.timetracker.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Home : Route{

        @Serializable
        data object Timer: Route

        @Serializable
        data object Calendar: Route

        @Serializable
        data object Reports: Route

        @Serializable
        data object Projects: Route

    }

    @Serializable
    data object Settings: Route
}
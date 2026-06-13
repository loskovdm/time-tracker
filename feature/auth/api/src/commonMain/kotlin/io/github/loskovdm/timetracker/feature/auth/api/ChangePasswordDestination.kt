package io.github.loskovdm.timetracker.feature.auth.api

import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDestination(
    val prefilledEmail: String? = null,
    val closeAuthOnSuccess: Boolean = false,
) : TimeTrackerDestination

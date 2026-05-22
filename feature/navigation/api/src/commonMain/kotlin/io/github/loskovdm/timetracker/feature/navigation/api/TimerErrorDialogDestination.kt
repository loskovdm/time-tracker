package io.github.loskovdm.timetracker.feature.navigation.api

data class TimerErrorDialogDestination(
    val errorMessage: String,
    val clearError: () -> Unit,
) : TimeTrackerDestination
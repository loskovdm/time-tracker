package io.github.loskovdm.timetracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.loskovdm.timetracker.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "TimeTracker",
        ) {
            TimeTrackerApp()
        }
    }
}
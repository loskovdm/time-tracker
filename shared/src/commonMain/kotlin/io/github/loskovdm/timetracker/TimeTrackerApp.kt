package io.github.loskovdm.timetracker

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import io.github.loskovdm.timetracker.navigation.RootNavigation

@Composable
@Preview
fun TimeTrackerApp() {
    MaterialTheme {
        RootNavigation()
    }
}
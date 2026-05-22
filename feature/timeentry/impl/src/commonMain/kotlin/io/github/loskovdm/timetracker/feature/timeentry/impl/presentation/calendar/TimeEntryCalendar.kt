package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryEditorDestination
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TimeEntryCalendar(onTimeEntryEditorClicked: (Uuid) -> Unit) {
    LazyColumn {
        items(25) {
            Text(
                text = "Item #$it",
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            HorizontalDivider()
        }
    }
}
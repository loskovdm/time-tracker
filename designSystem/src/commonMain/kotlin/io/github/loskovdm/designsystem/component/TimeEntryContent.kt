package io.github.loskovdm.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_description

@Composable
fun TimeEntryContent(
    modifier: Modifier = Modifier,
    taskName: String?,
    projectName: String?,
    projectColor: Long?,
) {
    Column(
        modifier = modifier,
    ) {
        TimeEntryTask(
            name = taskName,
        )
        TimeEntryProject(
            name = projectName,
            color = projectColor,
        )
    }
}

@Composable
fun TimeEntryTask(
    modifier: Modifier = Modifier,
    name: String?,
) {
    val isEmptyTask = name == null

    Text(
        modifier = modifier,
        text = name ?: stringResource(Res.string.add_description),
        style = if (isEmptyTask) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.titleMedium
        },
        fontWeight = if (isEmptyTask) {
            FontWeight.Normal
        } else {
            FontWeight.SemiBold
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun TimeEntryProject(
    modifier: Modifier = Modifier,
    name: String?,
    color: Long?,
) {
    if (name == null) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(color ?: 0xffffff))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
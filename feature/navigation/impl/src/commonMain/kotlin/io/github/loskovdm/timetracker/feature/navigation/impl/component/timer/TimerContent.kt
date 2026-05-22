package io.github.loskovdm.timetracker.feature.navigation.impl.component.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.component.TimeEntryContent
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntryWithRelations
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.active_timer
import timetracker.designsystem.generated.resources.ic_stop_filled
import timetracker.designsystem.generated.resources.stop
import timetracker.designsystem.generated.resources.stop_timer
import kotlin.time.Duration

@Composable
internal fun MobilePortraitTimer(
    modifier: Modifier = Modifier,
    activeTimeEntry: TimeEntryWithRelations,
    duration: Duration,
    onClick: () -> Unit,
    onStop: () -> Unit,
    shape: Shape,
    containerColor: Color,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TimerDuration(
                    duration = duration,
                )
                TimeEntryContent(
                    taskName = activeTimeEntry.task?.name,
                    projectName = activeTimeEntry.project?.name,
                    projectColor = activeTimeEntry.project?.color,
                )
            }
            TimerStopButton(
                onClick = onStop,
            )
        }
    }
}

@Composable
internal fun MobileLandscapeTimer(
    modifier: Modifier = Modifier,
    activeTimeEntry: TimeEntryWithRelations,
    duration: Duration,
    onClick: () -> Unit,
    onStop: () -> Unit,
    shape: Shape,
    containerColor: Color,
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(min = 280.dp, max = 340.dp),
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.active_timer),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = formatTimeToHmsString(duration.inWholeSeconds),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(24.dp))
                TimeEntryContent(
                    taskName = activeTimeEntry.task?.name,
                    projectName = activeTimeEntry.project?.name,
                    projectColor = activeTimeEntry.project?.color,
                )
            }
            TimerStopButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onStop,
            )
        }
    }
}

@Composable
internal fun ExpandedTimer(
    modifier: Modifier = Modifier,
    activeTimeEntry: TimeEntryWithRelations,
    duration: Duration,
    onClick: () -> Unit,
    onStop: () -> Unit,
    shape: Shape,
    containerColor: Color,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TimeEntryContent(
                modifier = Modifier.weight(1f),
                taskName = activeTimeEntry.task?.name,
                projectName = activeTimeEntry.project?.name,
                projectColor = activeTimeEntry.project?.color,
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = formatTimeToHmsString(duration.inWholeSeconds),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimerStopButton(
                onClick = onStop,
            )
        }
    }
}

@Composable
private fun TimerDuration(
    modifier: Modifier = Modifier,
    duration: Duration,
) {
    Text(
        modifier = modifier,
        text = formatTimeToHmsString(duration.inWholeSeconds),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun TimerStopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.error,
        ),
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_stop_filled),
            contentDescription = stringResource(Res.string.stop_timer),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(Res.string.stop),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)

package io.github.loskovdm.timetracker.feature.reports.impl

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.util.formatDateToString
import io.github.loskovdm.timetracker.feature.reports.impl.presentation.DateRange
import io.github.loskovdm.timetracker.feature.reports.impl.presentation.ReportPeriod
import io.github.loskovdm.timetracker.feature.reports.impl.presentation.ReportsUiState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.reports_all_projects
import timetracker.designsystem.generated.resources.reports_custom_end
import timetracker.designsystem.generated.resources.reports_custom_start
import timetracker.designsystem.generated.resources.reports_date_range
import timetracker.designsystem.generated.resources.reports_period_custom
import timetracker.designsystem.generated.resources.reports_period_day
import timetracker.designsystem.generated.resources.reports_period_month
import timetracker.designsystem.generated.resources.reports_period_week
import timetracker.designsystem.generated.resources.reports_period_year
import timetracker.designsystem.generated.resources.reports_select_projects
import timetracker.designsystem.generated.resources.ic_calendar_outlined
import timetracker.designsystem.generated.resources.ic_projects_outlined
import kotlin.uuid.ExperimentalUuidApi

@Composable
internal fun ReportsPeriodChips(
    selected: ReportPeriod,
    onSelected: (ReportPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReportPeriod.entries.forEach { period ->
            FilterChip(
                selected = selected == period,
                onClick = { onSelected(period) },
                label = { Text(text = periodLabel(period)) },
                shape = RoundedCornerShape(12.dp),
            )
        }
    }
}

@Composable
internal fun ReportsCustomDateRange(
    startDate: LocalDate,
    endDate: LocalDate,
    onStartChanged: (LocalDate) -> Unit,
    onEndChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReportsDatePickerButton(
                label = stringResource(Res.string.reports_custom_start),
                date = startDate,
                onDateSelected = onStartChanged,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "—",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ReportsDatePickerButton(
                label = stringResource(Res.string.reports_custom_end),
                date = endDate,
                onDateSelected = onEndChanged,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
internal fun ReportsDatePickerButton(
    label: String,
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val zone = TimeZone.UTC

    FilledTonalButton(
        onClick = { showPicker = true },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_calendar_outlined),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatDateToString(date),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }

    if (showPicker) {
        val deviceConfiguration = LocalDeviceConfiguration.current
        val datePickerState = remember(date) {
            DatePickerState(
                locale = CalendarLocale.getDefault(),
                initialSelectedDateMillis = date.atStartOfDayIn(zone).toEpochMilliseconds(),
                initialDisplayMode = if (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE) {
                    DisplayMode.Input
                } else {
                    DisplayMode.Picker
                },
            )
        }

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(
                                Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(zone)
                                    .date,
                            )
                        }
                        showPicker = false
                    },
                ) {
                    Text(text = stringResource(Res.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(text = stringResource(Res.string.cancel))
                }
            },
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
internal fun ReportsProjectsButton(
    state: ReportsUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_projects_outlined),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = projectSelectionLabel(state),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
internal fun ReportsDateRangeLabel(dateRange: DateRange) {
    val startText = formatDateToString(dateRange.start)
    val endText = formatDateToString(dateRange.end)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(Res.string.reports_date_range),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = if (dateRange.start == dateRange.end) startText else "$startText — $endText",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
internal fun periodLabel(period: ReportPeriod): String {
    return when (period) {
        ReportPeriod.DAY -> stringResource(Res.string.reports_period_day)
        ReportPeriod.WEEK -> stringResource(Res.string.reports_period_week)
        ReportPeriod.MONTH -> stringResource(Res.string.reports_period_month)
        ReportPeriod.YEAR -> stringResource(Res.string.reports_period_year)
        ReportPeriod.CUSTOM -> stringResource(Res.string.reports_period_custom)
    }
}

@Composable
internal fun projectSelectionLabel(state: ReportsUiState): String {
    return if (state.useAllProjects) {
        stringResource(Res.string.reports_all_projects)
    } else if (state.selectedProjectIds.isEmpty()) {
        stringResource(Res.string.reports_select_projects)
    } else {
        "${state.selectedProjectIds.size}"
    }
}

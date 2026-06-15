@file:OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)

package io.github.loskovdm.timetracker.feature.reports.impl.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.loskovdm.designsystem.component.LoadingScreen
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.local.LocalFabPadding
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.month_april_short
import timetracker.designsystem.generated.resources.month_august_short
import timetracker.designsystem.generated.resources.month_december_short
import timetracker.designsystem.generated.resources.month_february_short
import timetracker.designsystem.generated.resources.month_january_short
import timetracker.designsystem.generated.resources.month_july_short
import timetracker.designsystem.generated.resources.month_june_short
import timetracker.designsystem.generated.resources.month_march_short
import timetracker.designsystem.generated.resources.month_may_short
import timetracker.designsystem.generated.resources.month_november_short
import timetracker.designsystem.generated.resources.month_october_short
import timetracker.designsystem.generated.resources.month_september_short
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.reports_all_projects
import timetracker.designsystem.generated.resources.reports_no_data
import timetracker.designsystem.generated.resources.reports_period
import timetracker.designsystem.generated.resources.reports_projects
import timetracker.designsystem.generated.resources.reports_select_projects
import timetracker.designsystem.generated.resources.reports_time_by_day
import timetracker.designsystem.generated.resources.reports_time_by_project
import timetracker.designsystem.generated.resources.reports_total_time
import timetracker.designsystem.generated.resources.reports_unknown_project
import timetracker.designsystem.generated.resources.ic_arrow_drop_down
import timetracker.designsystem.generated.resources.ic_arrow_drop_up
import timetracker.designsystem.generated.resources.weekday_friday_short
import timetracker.designsystem.generated.resources.without_task
import timetracker.designsystem.generated.resources.weekday_monday_short
import timetracker.designsystem.generated.resources.weekday_saturday_short
import timetracker.designsystem.generated.resources.weekday_sunday_short
import timetracker.designsystem.generated.resources.weekday_thursday_short
import timetracker.designsystem.generated.resources.weekday_tuesday_short
import timetracker.designsystem.generated.resources.weekday_wednesday_short
import timetracker.designsystem.generated.resources.without_project
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
internal fun Reports(
    viewModel: ReportsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        ReportsState.Loading -> LoadingScreen()
        is ReportsState.Loaded -> ReportsContent(
            state = currentState.data,
            onPeriodSelected = viewModel::onPeriodSelected,
            onCustomStartChanged = viewModel::onCustomStartChanged,
            onCustomEndChanged = viewModel::onCustomEndChanged,
            onProjectSelectionUpdated = viewModel::onProjectSelectionUpdated,
        )
    }
}

@Composable
private fun ReportsContent(
    state: ReportsUiState,
    onPeriodSelected: (ReportPeriod) -> Unit,
    onCustomStartChanged: (LocalDate) -> Unit,
    onCustomEndChanged: (LocalDate) -> Unit,
    onProjectSelectionUpdated: (Boolean, Set<Uuid>) -> Unit,
) {
    var showProjectDialog by remember { mutableStateOf(false) }
    val deviceConfiguration = LocalDeviceConfiguration.current
    val isMobilePortrait = deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT
    val isTabletPortrait = deviceConfiguration == DeviceConfiguration.TABLET_PORTRAIT
    val isLandscapeOrDesktop = deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE ||
        deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE ||
        deviceConfiguration == DeviceConfiguration.DESKTOP

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp + LocalFabPadding.current.calculateBottomPadding(),
                )
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (isMobilePortrait) {
            ReportsFiltersCard(
                state = state,
                onPeriodSelected = onPeriodSelected,
                onCustomStartChanged = onCustomStartChanged,
                onCustomEndChanged = onCustomEndChanged,
                onProjectSelectionClick = { showProjectDialog = true },
            )

            ReportsSummaryCard(
                dateRange = state.dateRange,
                totalDurationSeconds = state.totalDurationSeconds,
            )

            ReportsBarChartCard(
                timeSeries = state.timeSeries,
                timeGranularity = state.timeGranularity,
            )

            ReportsPieChartCard(
                projectSeries = state.projectSeries,
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ReportsFiltersCard(
                    modifier = Modifier.weight(1f),
                    state = state,
                    onPeriodSelected = onPeriodSelected,
                    onCustomStartChanged = onCustomStartChanged,
                    onCustomEndChanged = onCustomEndChanged,
                    onProjectSelectionClick = { showProjectDialog = true },
                )

                ReportsSummaryCard(
                    modifier = Modifier.weight(1f),
                    dateRange = state.dateRange,
                    totalDurationSeconds = state.totalDurationSeconds,
                )
            }

            if (isLandscapeOrDesktop) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ReportsBarChartCard(
                        modifier = Modifier.weight(1f),
                        timeSeries = state.timeSeries,
                        timeGranularity = state.timeGranularity,
                    )

                    ReportsPieChartCard(
                        modifier = Modifier.weight(1f),
                        projectSeries = state.projectSeries,
                    )
                }
            } else if (isTabletPortrait) {
                ReportsBarChartCard(
                    timeSeries = state.timeSeries,
                    timeGranularity = state.timeGranularity,
                )

                ReportsPieChartCard(
                    projectSeries = state.projectSeries,
                )
            }
        }
    }

    if (showProjectDialog) {
        ProjectSelectionDialog(
            projects = state.availableProjects,
            useAllProjects = state.useAllProjects,
            selectedProjectIds = state.selectedProjectIds,
            onDismiss = { showProjectDialog = false },
            onApply = { useAllProjects, selectedProjectIds ->
                onProjectSelectionUpdated(useAllProjects, selectedProjectIds)
                showProjectDialog = false
            },
        )
    }
}

@Composable
private fun ReportsFiltersCard(
    modifier: Modifier = Modifier,
    state: ReportsUiState,
    onPeriodSelected: (ReportPeriod) -> Unit,
    onCustomStartChanged: (LocalDate) -> Unit,
    onCustomEndChanged: (LocalDate) -> Unit,
    onProjectSelectionClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(Res.string.reports_period),
                    style = MaterialTheme.typography.titleMedium,
                )
                ReportsPeriodChips(
                    selected = state.periodType,
                    onSelected = onPeriodSelected,
                )
                if (state.periodType == ReportPeriod.CUSTOM) {
                    ReportsCustomDateRange(
                        startDate = state.customStartDate,
                        endDate = state.customEndDate,
                        onStartChanged = onCustomStartChanged,
                        onEndChanged = onCustomEndChanged,
                    )
                } else {
                    ReportsDateRangeLabel(dateRange = state.dateRange)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(Res.string.reports_projects),
                    style = MaterialTheme.typography.titleMedium,
                )
                ReportsProjectsButton(
                    state = state,
                    onClick = onProjectSelectionClick,
                )
            }
        }
    }
}

@Composable
private fun ReportsSummaryCard(
    modifier: Modifier = Modifier,
    dateRange: DateRange,
    totalDurationSeconds: Long,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.reports_total_time),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = formatTimeToHmsString(totalDurationSeconds),
                style = MaterialTheme.typography.headlineSmall,
            )
            ReportsDateRangeLabel(dateRange)
        }
    }
}

@Composable
private fun ReportsBarChartCard(
    modifier: Modifier = Modifier,
    timeSeries: List<ReportTimeBucket>,
    timeGranularity: ReportGranularity,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.reports_time_by_day),
                style = MaterialTheme.typography.titleMedium,
            )
            ReportsBarChart(timeSeries, timeGranularity)
        }
    }
}

@Composable
private fun ReportsBarChart(
    timeSeries: List<ReportTimeBucket>,
    timeGranularity: ReportGranularity,
) {
    val maxValue = timeSeries.maxOfOrNull { it.durationSeconds } ?: 0L

    if (maxValue == 0L) {
        Text(
            text = stringResource(Res.string.reports_no_data),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        return
    }

    val xData = remember(timeSeries) { timeSeries.map { it.date } }
    val yData = remember(timeSeries) { timeSeries.map { it.durationSeconds.toFloat() } }
    val yMax = (yData.maxOrNull() ?: 0f).coerceAtLeast(1f)
    val showYearInLabels = timeSeries.any { it.date.year != timeSeries.first().date.year }
    val indexByDate = remember(xData) { xData.withIndex().associate { it.value to it.index } }

    XYGraph<LocalDate, Float>(
        xAxisModel = remember(xData) { CategoryAxisModel(xData) },
        yAxisModel = remember(yMax) {
            FloatLinearAxisModel(
                range = 0f..yMax,
                minimumMajorTickSpacing = 36.dp,
                allowZooming = false,
                allowPanning = false,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        panZoomEnabled = false,
        xAxisTitle = {},
        yAxisTitle = {},
        xAxisLabels = { date: LocalDate ->
            val index = indexByDate[date] ?: 0
            if (shouldShowAxisLabel(index, xData.size, timeGranularity)) {
                Text(
                    text = timeAxisLabel(date, timeGranularity, timeSeries.size, showYearInLabels),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            } else {
                Text("")
            }
        },
        yAxisLabels = { value: Float ->
            Text(
                text = formatTimeToHmsString(value.toLong()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
    ) {
        VerticalBarPlot(
            xData = xData,
            yData = yData,
            barWidth = 0.7f,
            bar = {
                DefaultVerticalBar(color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}

@Composable
private fun ReportsPieChartCard(
    modifier: Modifier = Modifier,
    projectSeries: List<ReportProjectSlice>,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.reports_time_by_project),
                style = MaterialTheme.typography.titleMedium,
            )
            ReportsPieChart(projectSeries)
        }
    }
}

@Composable
private fun ReportsPieChart(
    projectSeries: List<ReportProjectSlice>,
) {
    val total = projectSeries.sumOf { it.durationSeconds }

    if (total == 0L) {
        Text(
            text = stringResource(Res.string.reports_no_data),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        return
    }

    val fallbackColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
    )
    val values = projectSeries.map { it.durationSeconds.toFloat() }
    val sliceColors = projectSeries.mapIndexed { index, slice ->
        slice.color?.let { Color(it) } ?: fallbackColors[index % fallbackColors.size]
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center,
        ) {
            PieChart(
                values = values,
                modifier = Modifier.size(220.dp),
                holeSize = 0.58f,
                slice = { index ->
                    val sliceColor = sliceColors.getOrElse(index) {
                        fallbackColors[index % fallbackColors.size]
                    }
                    DefaultSlice(
                        color = sliceColor,
                        gap = 2f,
                    )
                },
                label = {},
            )
        }

        ReportsProjectBreakdown(
            projectSeries = projectSeries,
            fallbackColors = fallbackColors,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun ReportsProjectBreakdown(
    projectSeries: List<ReportProjectSlice>,
    fallbackColors: List<Color>,
) {
    var expandedKeys by remember { mutableStateOf(emptySet<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        projectSeries.forEachIndexed { index, slice ->
            val color = slice.color?.let { Color(it) } ?: fallbackColors[index % fallbackColors.size]
            val projectKey = slice.projectId?.toString() ?: "no_project"
            val isExpanded = projectKey in expandedKeys
            val canExpand = slice.tasks.isNotEmpty()
            val projectLabel = when {
                slice.projectId == null -> stringResource(Res.string.without_project)
                slice.projectName != null -> slice.projectName
                else -> stringResource(Res.string.reports_unknown_project)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isExpanded) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        } else {
                            Color.Transparent
                        },
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (canExpand) {
                                Modifier.clickable {
                                    expandedKeys = if (isExpanded) {
                                        expandedKeys - projectKey
                                    } else {
                                        expandedKeys + projectKey
                                    }
                                }
                            } else {
                                Modifier
                            },
                        )
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(color),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = projectLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = formatTimeToHmsString(slice.durationSeconds),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (canExpand) {
                            Icon(
                                imageVector = vectorResource(
                                    if (isExpanded) {
                                        Res.drawable.ic_arrow_drop_up
                                    } else {
                                        Res.drawable.ic_arrow_drop_down
                                    },
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 8.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        slice.tasks.forEach { task ->
                            ReportTaskRow(
                                task = task,
                                projectColor = color,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportTaskRow(
    task: ReportTaskSlice,
    projectColor: Color,
) {
    val taskLabel = when {
        task.taskId == null -> stringResource(Res.string.without_task)
        task.taskName != null -> task.taskName
        else -> stringResource(Res.string.without_task)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(projectColor.copy(alpha = 0.6f)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = taskLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = formatTimeToHmsString(task.durationSeconds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProjectSelectionDialog(
    projects: List<Project>,
    useAllProjects: Boolean,
    selectedProjectIds: Set<Uuid>,
    onDismiss: () -> Unit,
    onApply: (Boolean, Set<Uuid>) -> Unit,
) {
    var allSelected by remember(useAllProjects) { mutableStateOf(useAllProjects) }
    var currentSelected by remember(selectedProjectIds) { mutableStateOf(selectedProjectIds) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.reports_select_projects)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = allSelected,
                        onCheckedChange = { checked ->
                            allSelected = checked
                            if (checked) {
                                currentSelected = emptySet()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(Res.string.reports_all_projects))
                }

                projects.forEach { project ->
                    val isSelected = currentSelected.contains(project.id)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                allSelected = false
                                currentSelected = if (checked) {
                                    currentSelected + project.id
                                } else {
                                    currentSelected - project.id
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(project.color))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onApply(allSelected, currentSelected) }) {
                Text(text = stringResource(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
    )
}

@Composable
private fun timeAxisLabel(
    date: LocalDate,
    granularity: ReportGranularity,
    totalBuckets: Int,
    showYear: Boolean,
): String {
    return when (granularity) {
        ReportGranularity.DAY -> {
            if (totalBuckets <= 7) {
                when (date.dayOfWeek) {
                    DayOfWeek.MONDAY -> stringResource(Res.string.weekday_monday_short)
                    DayOfWeek.TUESDAY -> stringResource(Res.string.weekday_tuesday_short)
                    DayOfWeek.WEDNESDAY -> stringResource(Res.string.weekday_wednesday_short)
                    DayOfWeek.THURSDAY -> stringResource(Res.string.weekday_thursday_short)
                    DayOfWeek.FRIDAY -> stringResource(Res.string.weekday_friday_short)
                    DayOfWeek.SATURDAY -> stringResource(Res.string.weekday_saturday_short)
                    DayOfWeek.SUNDAY -> stringResource(Res.string.weekday_sunday_short)
                }
            } else {
                date.day.toString()
            }
        }
        ReportGranularity.MONTH -> {
            val base = monthShortName(date)
            if (showYear) "$base ${date.year}" else base
        }
    }
}

@Composable
private fun monthShortName(date: LocalDate): String {
    return when (date.month) {
        Month.JANUARY -> stringResource(Res.string.month_january_short)
        Month.FEBRUARY -> stringResource(Res.string.month_february_short)
        Month.MARCH -> stringResource(Res.string.month_march_short)
        Month.APRIL -> stringResource(Res.string.month_april_short)
        Month.MAY -> stringResource(Res.string.month_may_short)
        Month.JUNE -> stringResource(Res.string.month_june_short)
        Month.JULY -> stringResource(Res.string.month_july_short)
        Month.AUGUST -> stringResource(Res.string.month_august_short)
        Month.SEPTEMBER -> stringResource(Res.string.month_september_short)
        Month.OCTOBER -> stringResource(Res.string.month_october_short)
        Month.NOVEMBER -> stringResource(Res.string.month_november_short)
        Month.DECEMBER -> stringResource(Res.string.month_december_short)
    }
}

private fun shouldShowAxisLabel(
    index: Int,
    total: Int,
    granularity: ReportGranularity,
): Boolean {
    val maxLabels = when (granularity) {
        ReportGranularity.DAY -> 7
        ReportGranularity.MONTH -> 12
    }
    if (total <= maxLabels) return true

    val step = ceil(total.toDouble() / maxLabels).toInt().coerceAtLeast(1)
    return index % step == 0 || index == total - 1
}

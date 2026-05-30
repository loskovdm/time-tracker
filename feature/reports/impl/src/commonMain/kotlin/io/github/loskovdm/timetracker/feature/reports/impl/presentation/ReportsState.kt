package io.github.loskovdm.timetracker.feature.reports.impl.presentation

import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface ReportsState {
    data object Loading : ReportsState

    data class Loaded(
        val data: ReportsUiState,
    ) : ReportsState
}

@OptIn(ExperimentalUuidApi::class)
data class ReportsUiState(
    val periodType: ReportPeriod,
    val dateRange: DateRange,
    val availableProjects: List<Project>,
    val useAllProjects: Boolean,
    val selectedProjectIds: Set<Uuid>,
    val customStartDate: LocalDate,
    val customEndDate: LocalDate,
    val timeSeries: List<ReportTimeBucket>,
    val timeGranularity: ReportGranularity,
    val projectSeries: List<ReportProjectSlice>,
    val totalDurationSeconds: Long,
)

data class DateRange(
    val start: LocalDate,
    val end: LocalDate,
)

@OptIn(ExperimentalUuidApi::class)
data class ReportTimeBucket(
    val date: LocalDate,
    val durationSeconds: Long,
)

@OptIn(ExperimentalUuidApi::class)
data class ReportProjectSlice(
    val projectId: Uuid?,
    val projectName: String?,
    val durationSeconds: Long,
    val color: Long?,
    val tasks: List<ReportTaskSlice> = emptyList(),
)

@OptIn(ExperimentalUuidApi::class)
data class ReportTaskSlice(
    val taskId: Uuid?,
    val taskName: String?,
    val durationSeconds: Long,
)

enum class ReportPeriod {
    DAY,
    WEEK,
    MONTH,
    YEAR,
    CUSTOM,
}

enum class ReportGranularity {
    DAY,
    MONTH,
}

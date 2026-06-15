package io.github.loskovdm.timetracker.feature.reports.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.model.TimeEntryWithRelations
import io.github.loskovdm.domain.usecase.project.GetActiveProjectsUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetCompletedTimeEntriesUseCase
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.reports.impl.mapper.ProjectMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class ReportsViewModel(
    private val projectMapper: ProjectMapper,
    getCompletedTimeEntriesUseCase: GetCompletedTimeEntriesUseCase,
    getActiveProjectsUseCase: GetActiveProjectsUseCase,
) : ViewModel() {

    private val timeZone = TimeZone.currentSystemDefault()

    private val filterState = MutableStateFlow(createInitialFilterState())

    private val projectsFlow = getActiveProjectsUseCase()
        .map { projects ->
            projects
                .sortedBy { it.name.lowercase() }
                .map { projectMapper.toView(it) }
        }

    val state: StateFlow<ReportsState> = combine(
        filterState,
        getCompletedTimeEntriesUseCase(),
        projectsFlow,
    ) { filter, completedEntries, projects ->
        val dateRange = resolveDateRange(filter)
        val granularity = resolveGranularity(filter.periodType, dateRange)
        val activeProjectIds = projects.map { it.id }.toSet()
        val filteredEntries = filterEntries(
            entries = completedEntries,
            dateRange = dateRange,
            useAllProjects = filter.useAllProjects,
            selectedProjectIds = filter.selectedProjectIds,
            activeProjectIds = activeProjectIds,
        )

        val timeSeries = buildTimeSeries(dateRange, filteredEntries, granularity)
        val projectSeries = buildProjectSeries(filteredEntries, projects)
        val totalDurationSeconds = timeSeries.sumOf { it.durationSeconds }

        ReportsState.Loaded(
            data = ReportsUiState(
                periodType = filter.periodType,
                dateRange = dateRange,
                availableProjects = projects,
                useAllProjects = filter.useAllProjects,
                selectedProjectIds = filter.selectedProjectIds,
                customStartDate = filter.customStartDate,
                customEndDate = filter.customEndDate,
                timeSeries = timeSeries,
                timeGranularity = granularity,
                projectSeries = projectSeries,
                totalDurationSeconds = totalDurationSeconds,
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportsState.Loading,
        )

    fun onPeriodSelected(period: ReportPeriod) {
        filterState.value = filterState.value.copy(periodType = period)
    }

    fun onCustomStartChanged(date: LocalDate) {
        filterState.value = filterState.value.copy(customStartDate = date)
    }

    fun onCustomEndChanged(date: LocalDate) {
        filterState.value = filterState.value.copy(customEndDate = date)
    }

    fun onProjectSelectionUpdated(useAllProjects: Boolean, selectedProjectIds: Set<Uuid>) {
        filterState.value = filterState.value.copy(
            useAllProjects = useAllProjects,
            selectedProjectIds = selectedProjectIds,
        )
    }

    private fun buildTimeSeries(
        dateRange: DateRange,
        entries: List<TimeEntryWithRelations>,
        granularity: ReportGranularity,
    ): List<ReportTimeBucket> {
        val entriesByDate = entries.groupBy { entry ->
            entry.timeEntry.startDateTime.toLocalDateTime(timeZone).date
        }

        return when (granularity) {
            ReportGranularity.DAY -> {
                generateDates(dateRange.start, dateRange.end).map { date ->
                    ReportTimeBucket(
                        date = date,
                        durationSeconds = entriesByDate[date]?.sumOf { entry ->
                            (entry.timeEntry.endDateTime!! - entry.timeEntry.startDateTime).inWholeSeconds
                        } ?: 0L,
                    )
                }
            }
            ReportGranularity.MONTH -> {
                generateMonths(dateRange.start, dateRange.end).map { monthStart ->
                    val monthEntries = entriesByDate
                        .filterKeys { date ->
                            date.year == monthStart.year && date.month == monthStart.month
                        }
                        .values
                        .flatten()

                    ReportTimeBucket(
                        date = monthStart,
                        durationSeconds = monthEntries.sumOf { entry ->
                            (entry.timeEntry.endDateTime!! - entry.timeEntry.startDateTime).inWholeSeconds
                        }
                    )
                }
            }
        }
    }

    private fun resolveGranularity(
        period: ReportPeriod,
        dateRange: DateRange,
    ): ReportGranularity {
        return when (period) {
            ReportPeriod.YEAR -> ReportGranularity.MONTH
            ReportPeriod.CUSTOM -> {
                val days = countDaysInclusive(dateRange.start, dateRange.end)
                if (days > 62) ReportGranularity.MONTH else ReportGranularity.DAY
            }
            else -> ReportGranularity.DAY
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun buildProjectSeries(
        entries: List<TimeEntryWithRelations>,
        projects: List<Project>,
    ): List<ReportProjectSlice> {
        val projectsById = projects.associateBy { it.id }
        val groupedByProject = entries.groupBy { it.timeEntry.projectId }

        return groupedByProject
            .mapNotNull { (projectId, groupedEntries) ->
                if (projectId != null && !projectsById.containsKey(projectId)) {
                    return@mapNotNull null
                }
                val project = projectId?.let { projectsById[it] }
                val tasks = groupedEntries
                    .groupBy { it.timeEntry.taskId }
                    .map { (taskId, taskEntries) ->
                        ReportTaskSlice(
                            taskId = taskId,
                            taskName = taskEntries.firstOrNull()?.task?.name,
                            durationSeconds = taskEntries.sumOf { entry ->
                                (entry.timeEntry.endDateTime!! - entry.timeEntry.startDateTime).inWholeSeconds
                            },
                        )
                    }
                    .sortedByDescending { it.durationSeconds }

                ReportProjectSlice(
                    projectId = projectId,
                    projectName = project?.name,
                    durationSeconds = groupedEntries.sumOf { entry ->
                        (entry.timeEntry.endDateTime!! - entry.timeEntry.startDateTime).inWholeSeconds
                    },
                    color = project?.color,
                    tasks = tasks,
                )
            }
            .sortedByDescending { it.durationSeconds }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun filterEntries(
        entries: List<TimeEntryWithRelations>,
        dateRange: DateRange,
        useAllProjects: Boolean,
        selectedProjectIds: Set<Uuid>,
        activeProjectIds: Set<Uuid>,
    ): List<TimeEntryWithRelations> {
        return entries
            .filter { entry ->
                val entryDate = entry.timeEntry.startDateTime.toLocalDateTime(timeZone).date
                entryDate >= dateRange.start && entryDate <= dateRange.end
            }
            .filter { entry ->
                entry.timeEntry.projectId == null || activeProjectIds.contains(entry.timeEntry.projectId)
            }
            .filter { entry ->
                if (useAllProjects) {
                    true
                } else {
                    entry.timeEntry.projectId != null &&
                        selectedProjectIds.contains(entry.timeEntry.projectId)
                }
            }
    }

    private fun generateDates(start: LocalDate, end: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var currentDate = start
        while (currentDate <= end) {
            dates.add(currentDate)
            currentDate = currentDate.plus(DatePeriod(days = 1))
        }
        return dates
    }

    private fun generateMonths(start: LocalDate, end: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var current = LocalDate(start.year, start.month, 1)
        val endMonth = LocalDate(end.year, end.month, 1)
        while (current <= endMonth) {
            dates.add(current)
            current = current.nextMonthStart()
        }
        return dates
    }

    private fun LocalDate.nextMonthStart(): LocalDate {
        val nextMonth = if (month == Month.DECEMBER) Month.JANUARY else Month.entries[month.ordinal + 1]
        val nextYear = if (month == Month.DECEMBER) year + 1 else year
        return LocalDate(nextYear, nextMonth, 1)
    }

    private fun countDaysInclusive(start: LocalDate, end: LocalDate): Int {
        var count = 0
        var current = start
        while (current <= end) {
            count += 1
            current = current.plus(DatePeriod(days = 1))
        }
        return count
    }

    private fun resolveDateRange(filter: ReportsFilterState): DateRange {
        return when (filter.periodType) {
            ReportPeriod.DAY -> DateRange(filter.anchorDate, filter.anchorDate)
            ReportPeriod.WEEK -> DateRange(
                start = filter.anchorDate.startOfIsoWeek(),
                end = filter.anchorDate,
            )
            ReportPeriod.MONTH -> DateRange(
                start = LocalDate(filter.anchorDate.year, filter.anchorDate.month, 1),
                end = filter.anchorDate,
            )
            ReportPeriod.YEAR -> DateRange(
                start = LocalDate(filter.anchorDate.year, 1, 1),
                end = filter.anchorDate,
            )
            ReportPeriod.CUSTOM -> {
                val start = filter.customStartDate
                val end = filter.customEndDate
                if (start <= end) {
                    DateRange(start, end)
                } else {
                    DateRange(end, start)
                }
            }
        }
    }

    private fun createInitialFilterState(): ReportsFilterState {
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val weekStart = today.startOfIsoWeek()

        return ReportsFilterState(
            periodType = ReportPeriod.WEEK,
            anchorDate = today,
            customStartDate = weekStart,
            customEndDate = today,
            useAllProjects = true,
            selectedProjectIds = emptySet(),
        )
    }
}

private fun LocalDate.startOfIsoWeek(): LocalDate =
    minus(DatePeriod(days = dayOfWeek.ordinal))

@OptIn(ExperimentalUuidApi::class)
private data class ReportsFilterState(
    val periodType: ReportPeriod,
    val anchorDate: LocalDate,
    val customStartDate: LocalDate,
    val customEndDate: LocalDate,
    val useAllProjects: Boolean,
    val selectedProjectIds: Set<Uuid>,
)



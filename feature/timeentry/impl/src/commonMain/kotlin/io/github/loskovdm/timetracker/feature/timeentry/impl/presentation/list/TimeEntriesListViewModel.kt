package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.usecase.timeentry.GetCompletedTimeEntriesUseCase
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TimeEntryWithRelationsMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class TimeEntriesListViewModel(
    private val mapper: TimeEntryWithRelationsMapper,
    getCompletedTimeEntriesUseCase: GetCompletedTimeEntriesUseCase,
) : ViewModel() {
    val state: StateFlow<TimeEntriesListState> = getCompletedTimeEntriesUseCase()
        .map { completedTimeEntriesList ->
            if (completedTimeEntriesList.isEmpty()) {
                TimeEntriesListState.Empty
            } else {
                TimeEntriesListState.Loaded(
                    completedTimeEntries = mapper.toView(
                        completedTimeEntriesList
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimeEntriesListState.Loading
        )
}
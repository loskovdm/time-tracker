package io.github.loskovdm.timetracker.feature.timeentry.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntriesListDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryCalendarDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryEditorDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerViewModel
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TaskMapper
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TimeEntryMapper
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TimeEntryWithRelationsMapper
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.calendar.TimeEntryCalendar
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.editor.TimeEntryEditor
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.editor.TimeEntryEditorViewModel
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.list.TimeEntriesList
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.list.TimeEntriesListViewModel
import io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.timer.TimerViewModelImpl
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
val timeEntryModule = module {

    navigation<TimeEntriesListDestination>(
        metadata = EntryMetadataBuilder.timeEntriesList()
    ) {
        TimeEntriesList(
            onTimeEntryClicked = { timeEntryId ->
                val timeEntryEditorDestination = TimeEntryEditorDestination(timeEntryId)
                get<Navigator>().goTo(timeEntryEditorDestination)
            }
        )
    }

    navigation<TimeEntryCalendarDestination>(
        metadata = EntryMetadataBuilder.timeEntryCalendar()
    ) {
        TimeEntryCalendar(
            onTimeEntryEditorClicked = { timeEntryId ->
                val timeEntryEditorDestination = TimeEntryEditorDestination(timeEntryId)
                get<Navigator>().goTo(timeEntryEditorDestination)
            }
        )
    }

    navigation<TimeEntryEditorDestination>(
        metadata = EntryMetadataBuilder.editor()
    ) { key ->
        TimeEntryEditor(
            onClose = {
                get<Navigator>().goBack()
            },
            editorViewModel = koinViewModel { parametersOf(key.timeEntryId) }
        )
    }

    single<TimeEntryMapper>()
    single<ProjectMapper>()
    single<TaskMapper>()
    single<TimeEntryWithRelationsMapper>()

    single<TimerViewModelImpl>() bind TimerViewModel::class
    viewModel<TimeEntriesListViewModel>()
    viewModel<TimeEntryEditorViewModel>()

    includes(domainModule)
}
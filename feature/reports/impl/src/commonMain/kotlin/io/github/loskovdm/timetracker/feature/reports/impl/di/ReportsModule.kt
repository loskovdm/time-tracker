package io.github.loskovdm.timetracker.feature.reports.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.reports.api.ReportsDestination
import io.github.loskovdm.timetracker.feature.reports.impl.presentation.Reports
import io.github.loskovdm.timetracker.feature.reports.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.reports.impl.presentation.ReportsViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val reportsModule = module {
    navigation<ReportsDestination>(
        metadata = EntryMetadataBuilder.reports()
    ) {
        Reports()
    }

    single<ProjectMapper>()
    viewModel<ReportsViewModel>()

    includes(domainModule)
}
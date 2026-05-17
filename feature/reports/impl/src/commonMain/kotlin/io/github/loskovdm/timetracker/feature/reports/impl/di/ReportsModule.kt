package io.github.loskovdm.timetracker.feature.reports.impl.di

import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.reports.api.ReportsDestination
import io.github.loskovdm.timetracker.feature.reports.impl.Reports
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val reportsModule = module {
    navigation<ReportsDestination>(
        metadata = EntryMetadataBuilder.reports()
    ) {
        Reports()
    }
}
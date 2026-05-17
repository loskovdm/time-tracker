package io.github.loskovdm.timetracker.feature.settings.impl.di

import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.settings.api.SettingsDestination
import io.github.loskovdm.timetracker.feature.settings.impl.SettingsScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val settingsModule = module {
    navigation<SettingsDestination>(
        metadata = EntryMetadataBuilder.settings()
    ) {
        SettingsScreen()
    }
}

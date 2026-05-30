package io.github.loskovdm.timetracker.feature.settings.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.settings.api.SettingsDestination
import io.github.loskovdm.timetracker.feature.settings.impl.SettingsScreen
import io.github.loskovdm.timetracker.feature.settings.impl.presentation.SettingsViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val settingsModule = module {
    includes(domainModule)

    navigation<SettingsDestination>(
        metadata = EntryMetadataBuilder.settings(),
    ) {
        SettingsScreen()
    }

    viewModel<SettingsViewModel>()
}

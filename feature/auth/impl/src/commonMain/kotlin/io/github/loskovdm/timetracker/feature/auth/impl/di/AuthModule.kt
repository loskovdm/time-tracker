package io.github.loskovdm.timetracker.feature.auth.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.auth.api.AuthDestination
import io.github.loskovdm.timetracker.feature.auth.impl.AuthScreen
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.AuthViewModel
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val authModule = module {
    includes(domainModule)

    navigation<AuthDestination>(
        metadata = EntryMetadataBuilder.auth(),
    ) {
        AuthScreen(navigator = get())
    }

    viewModel<AuthViewModel>()
}

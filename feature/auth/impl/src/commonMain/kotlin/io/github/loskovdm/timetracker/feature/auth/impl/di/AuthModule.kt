package io.github.loskovdm.timetracker.feature.auth.impl.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timetracker.feature.auth.api.AuthDestination
import io.github.loskovdm.timetracker.feature.auth.api.ChangePasswordDestination
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.auth.AuthScreen
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.changepassword.ChangePasswordScreen
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.auth.AuthViewModel
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.changepassword.ChangePasswordViewModel
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadataBuilder
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
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

    navigation<ChangePasswordDestination>(
        metadata = EntryMetadataBuilder.changePassword(),
    ) { destination ->
        ChangePasswordScreen(
            destination = destination,
            navigator = get(),
            viewModel = koinViewModel { parametersOf(destination) },
        )
    }

    viewModel<AuthViewModel>()
    viewModel<ChangePasswordViewModel>()
}
